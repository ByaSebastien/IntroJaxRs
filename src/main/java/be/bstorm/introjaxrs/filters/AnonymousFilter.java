package be.bstorm.introjaxrs.filters;

import be.bstorm.introjaxrs.annotations.security.IsAnonymous;
import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Priority(2)
@IsAnonymous
@Provider
public class AnonymousFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        SecurityContext securityContext = requestContext.getSecurityContext();
        if(securityContext.getUserPrincipal() != null) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("{ \"error\": \"Unauthorized\" }")
                            .build()
            );
        }
    }
}
