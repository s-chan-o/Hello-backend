package hellobackend.skills.config.auth;

import hellobackend.skills.model.User;
import hellobackend.skills.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Optional을 통해 유저가 존재하는지 확인
        User user = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다: " + username));

        return new PrincipalDetails(user);
    }
}
