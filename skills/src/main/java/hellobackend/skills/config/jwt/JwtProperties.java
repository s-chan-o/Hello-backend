package hellobackend.skills.config.jwt;

public interface JwtProperties {

    String SECRET = "SECRET"; //자기만 아는 비밀값
    int EXPIRATION_TIME = 0;
    String HEADER_STRING = "HEADER ";
    String TOKEN_PREFIX = "TOKEN";
}
