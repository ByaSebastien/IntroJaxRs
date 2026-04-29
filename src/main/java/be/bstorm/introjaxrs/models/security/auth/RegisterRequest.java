package be.bstorm.introjaxrs.models.security.auth;

import be.bstorm.introjaxrs.pojos.User;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank
        String email,
        @NotBlank
        String username,
        @NotBlank
        String password
) {

    public User toUser()
    {
        return new User()
                .setEmail(email)
                .setUsername(username)
                .setPassword(password);
    }
}
