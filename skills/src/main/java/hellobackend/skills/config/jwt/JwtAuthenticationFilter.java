package hellobackend.skills.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음.
//login 요청해서 username, password 전송하면 동작
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    // /login 요청을 하면 실행
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter: 로그인 시도 중");

        //username, password를 받고 로그인 authenticationManager로 로그인을 시도
        //PrincipalDetailsService가 호출 -> loadUserByUsername() 실행
        //PrincipalDetails를 세션에 담고 (권한 관리를 위함) JWT토큰을 만든 후 응답

        return super.attemptAuthentication(request, response);
    }
}
