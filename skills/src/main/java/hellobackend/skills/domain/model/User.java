package hellobackend.skills.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "사용자 이름은 필수입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password;

    @ElementCollection
    private List<Role> roles;

    public enum Role {
        USER,
        ADMIN
    }

    public List<GrantedAuthority> getAuthorities() {
        return getRoleList().stream()
                .distinct()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    public List<Role> getRoleList() {
        if (roles != null && !roles.isEmpty()) {
            return roles;
        }
        return Collections.emptyList();
    }

}
