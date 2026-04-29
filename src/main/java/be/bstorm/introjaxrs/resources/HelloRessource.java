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

/**
 * Ressource JAX-RS pour les endpoints de test/hello.
 *
 * Fournit un endpoint simple pour vérifier l'authentification.
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@Path("/hello")
public class HelloRessource {

    /**
     * Endpoint hello sécurisé - nécessite une authentification.
     *
     * @param securityContext le contexte de sécurité contenant l'utilisateur authentifié
     * @return une réponse JSON avec un message de greeting personnalisé
     *
     * @throws jakarta.ws.rs.ForbiddenException si l'utilisateur n'est pas authentifié
     */
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
