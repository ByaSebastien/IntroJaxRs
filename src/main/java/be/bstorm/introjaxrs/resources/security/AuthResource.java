package be.bstorm.introjaxrs.resources.security;

import be.bstorm.introjaxrs.annotations.security.IsAnonymous;
import be.bstorm.introjaxrs.models.security.auth.LoginRequest;
import be.bstorm.introjaxrs.models.security.auth.RegisterRequest;
import be.bstorm.introjaxrs.models.security.auth.UserResponse;
import be.bstorm.introjaxrs.models.security.auth.UserTokenResponse;
import be.bstorm.introjaxrs.pojos.User;
import be.bstorm.introjaxrs.services.security.AuthService;
import be.bstorm.introjaxrs.utils.JwtUtils;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Ressource JAX-RS pour l'authentification et l'autorisation.
 *
 * Fournit les endpoints pour l'enregistrement et la connexion des utilisateurs.
 * Endpoints publics (accessibles sans authentification).
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@Path("/auth")
public class AuthResource {

    /**
     * Service d'authentification métier
     */
    @Inject
    private AuthService authService;

    /**
     * Validateur Jakarta pour les contraintes de validation
     */
    @Inject
    private Validator validator;

    /**
     * Utilitaire de gestion des JWT
     */
    @Inject
    private JwtUtils jwtUtils;

    /**
     * Enregistre un nouvel utilisateur.
     *
     * Endpoint public (accessible sans authentification).
     * Valide les données d'entrée avant création de l'utilisateur.
     *
     * @param request la requête d'enregistrement avec email, username et password
     * @return une réponse 200 OK en cas de succès
     *
     * @response 200 Utilisateur créé avec succès
     * @response 400 Données invalides ou email/username en doublon
     *
     * @throws RuntimeException si l'email ou le username existent déjà
     */
    @IsAnonymous
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(
            RegisterRequest request
    ) {
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            List<String> errors = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .toList();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("errors", errors))
                    .build();
        }

        authService.register(request);
        return Response.ok().build();
    }

    /**
     * Authentifie un utilisateur et retourne un token JWT.
     *
     * Endpoint public (accessible sans authentification).
     * Accepte l'email ou le username comme identifiant.
     * Retourne un token JWT valide pendant 15 minutes.
     *
     * @param request la requête de connexion avec login (email ou username) et password
     * @return une réponse 200 OK avec le token JWT et les informations utilisateur
     *
     * @response 200 Authentification réussie avec token JWT
     * @response 400 Données invalides ou identifiants incorrects
     */
    @IsAnonymous
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(
            LoginRequest request
    ) {
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            List<String> errors = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .toList();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("errors", errors))
                    .build();
        }

        User user = authService.login(request);
        UserResponse userResponse = UserResponse.fromUser(user);
        String token = jwtUtils.generateToken(user);
        UserTokenResponse response = new UserTokenResponse(userResponse, token);

        return Response.ok(response).build();
    }
}
