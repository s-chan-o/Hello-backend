package hellobackend.skills.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import hellobackend.skills.config.auth.PrincipalDetails;
import hellobackend.skills.model.User;
import hellobackend.skills.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

// JWT 기반 인증을 위한 필터
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    // 인증 또는 권한이 필요한 요청에 대해 필터링
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);

        // JWT 헤더가 존재하는지 확인
        if (jwtHeader != null && jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            String jwtToken = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, "");

            // JWT 토큰을 검증하여 사용자 정보를 가져옴
            String username;
            try {
                username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                        .build()
                        .verify(jwtToken)
                        .getClaim("username")
                        .asString();
            } catch (Exception e) {
                chain.doFilter(request, response);
                return; // JWT 검증 실패 시 필터 체인 중단
            }

            // 사용자 정보가 유효할 경우
            if (username != null) {
                Optional<User> userOptional = userRepository.findByUsername(username);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    PrincipalDetails principalDetails = new PrincipalDetails(user);
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                    // SecurityContext에 Authentication 객체 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
