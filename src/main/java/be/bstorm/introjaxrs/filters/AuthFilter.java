package be.bstorm.introjaxrs.filters;

import be.bstorm.introjaxrs.config.SecurityContextImpl;
import be.bstorm.introjaxrs.models.security.auth.UserResponse;
import be.bstorm.introjaxrs.utils.JwtUtils;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Priority(1)
@PreMatching
@Provider
public class AuthFilter implements ContainerRequestFilter {

    @Inject
    private JwtUtils jwtUtils;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String authorization = requestContext.getHeaderString("Authorization");

        if(authorization != null && authorization.startsWith("Bearer ")) {

            String token = authorization.substring(7);

            UserResponse user = jwtUtils.getUser(token);

            SecurityContext securityContext = new SecurityContextImpl(user, token);

            requestContext.setSecurityContext(securityContext);
        }
    }
}
