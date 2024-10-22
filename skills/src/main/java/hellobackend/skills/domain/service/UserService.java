package hellobackend.skills.domain.service;

import hellobackend.skills.domain.dto.UserDto;
import hellobackend.skills.domain.model.User;
import hellobackend.skills.domain.repository.UserRepository;
import hellobackend.skills.global.auth.PrincipalDetails;
import hellobackend.skills.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    // 회원가입
    public String signUp(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(List.of(User.Role.USER));
        userRepository.save(user);
        return "회원가입 완료";
    }

    // 로그인
    public String login(UserDto userDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return jwtProvider.createToken(principalDetails);
    }

    // 로그아웃
    public String logout() {
        SecurityContextHolder.clearContext();
        return "로그아웃 완료";
    }

    // 토큰 재발급
    public String reissueToken(String oldToken) {
        String username = jwtProvider.validateAndGetUsername(oldToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        PrincipalDetails principalDetails = new PrincipalDetails(user);
        return jwtProvider.createToken(principalDetails);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
