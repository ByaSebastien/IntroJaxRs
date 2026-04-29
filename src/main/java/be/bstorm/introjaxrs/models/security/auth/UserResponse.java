package be.bstorm.introjaxrs.models.security.auth;

import be.bstorm.introjaxrs.pojos.Role;
import be.bstorm.introjaxrs.pojos.User;

import java.util.List;

public record UserResponse(
        Integer id,
        String email,
        String username,
        List<String> roles
) {

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRoles().stream().map(Role::getName).toList()
        );
    }
}
