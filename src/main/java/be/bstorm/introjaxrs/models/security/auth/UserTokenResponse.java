package be.bstorm.introjaxrs.models.security.auth;

public record UserTokenResponse(
        UserResponse user,
        String token
) {
}
