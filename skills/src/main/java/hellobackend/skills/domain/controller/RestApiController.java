package hellobackend.skills.domain.controller;

import hellobackend.skills.domain.dto.UserDto;
import hellobackend.skills.domain.model.User;
import hellobackend.skills.domain.service.UserService;
import hellobackend.skills.global.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class RestApiController {

    private final UserService userService;

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

    @GetMapping("manager/reports")
    public String reports() {
        return "reports";
    }

    @GetMapping("admin/users")
    public List<User> users() {
        return userService.getAllUsers();
    }

    // 회원가입
    @PostMapping("join")
    public String join(@RequestBody UserDto userDto) {
        return userService.signUp(userDto);
    }

    // 로그인
    @PostMapping("login")
    public String login(@RequestBody UserDto userDto) {
        return userService.login(userDto);
    }

    // 로그아웃
    @PostMapping("logout")
    public String logout() {
        return userService.logout();
    }

    // 토큰 재발급
    @PostMapping("token/reissue")
    public String reissueToken(@RequestParam String oldToken) {
        return userService.reissueToken(oldToken);
    }
}
