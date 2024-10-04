package hellobackend.skills.global.jwt;

public interface JwtProperties {

    String SECRET = "SECRET"; // 비밀 키
    long EXPIRATION_TIME = 864_000_000; // 10일
    String HEADER_STRING = "Authorization";
    String TOKEN_PREFIX = "Bearer ";
}
