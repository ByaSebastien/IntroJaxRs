package be.bstorm.introjaxrs.resources;

import be.bstorm.introjaxrs.annotations.security.IsAuthenticated;
import be.bstorm.introjaxrs.models.security.auth.UserResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/hello")
public class HelloRessource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @IsAuthenticated
    public Response hello(
            @Context SecurityContext securityContext
    ) {

        UserResponse user = (UserResponse) securityContext.getUserPrincipal();

        return Response.ok()
                .entity("Hello " +  user.getName())
                .build();
    }
}
