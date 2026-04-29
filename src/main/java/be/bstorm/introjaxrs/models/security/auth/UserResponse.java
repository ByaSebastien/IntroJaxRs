package be.bstorm.introjaxrs.models.security.auth;

import be.bstorm.introjaxrs.pojos.Role;
import be.bstorm.introjaxrs.pojos.User;
import lombok.*;

import java.security.Principal;
import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode @ToString
public class UserResponse implements Principal {

    private Integer id;
    private String email;
    private String username;
    private List<String> roles;

    @Override
    public String getName() {
        return username;
    }

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRoles().stream().map(Role::getName).toList()
        );
    }
}
