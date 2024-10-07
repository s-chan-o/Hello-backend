package hellobackend.skills.domain.controller;

import hellobackend.skills.domain.repository.UserRepository;
import hellobackend.skills.global.auth.PrincipalDetails;
import hellobackend.skills.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class RestApiController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("home")
    public String home() {
        return "home";
    }

    @GetMapping("user")
    public String user(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("UserID: " + principal.getUser().getId());
        System.out.println("Username: " + principal.getUser().getUsername());

        return "user";
    }

    // 매니저 or 어드민 접근 가능
    @GetMapping("manager/reports")
    public String reports() {
        return "reports";
    }

    // 어드민 접근 가능
    @GetMapping("admin/users")
    public List<User> users() {
        return userRepository.findAll();
    }


    @PostMapping("join")
    public String join(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(User.Role.USER));
        userRepository.save(user);
        return "회원가입 완료";
    }
}
