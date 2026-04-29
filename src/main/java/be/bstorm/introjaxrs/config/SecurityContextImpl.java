package be.bstorm.introjaxrs.config;

import be.bstorm.introjaxrs.models.security.auth.UserResponse;
import be.bstorm.introjaxrs.pojos.Role;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

public class SecurityContextImpl implements SecurityContext {

    private final UserResponse user;
    private final String token;

    public SecurityContextImpl(UserResponse user, String token) {
        this.user = user;
        this.token = token;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        return this.user.getRoles().contains(role);
    }

    @Override
    public boolean isSecure() {
        return true;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer " + token;
    }
}
