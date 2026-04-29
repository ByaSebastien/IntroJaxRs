package be.bstorm.introjaxrs.filters;

import be.bstorm.introjaxrs.annotations.security.HasAuthority;
import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;

@Priority(2)
@HasAuthority
@Provider
public class HasAuthorityFilter implements ContainerRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        SecurityContext securityContext = requestContext.getSecurityContext();

        Method method = resourceInfo.getResourceMethod();

        HasAuthority hasAuthority = method.getAnnotation(HasAuthority.class);

        if(!securityContext.isUserInRole(hasAuthority.roles())) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("{ \"error\": \"Unauthorized\" }")
                            .build()
            );
        }
    }
}
