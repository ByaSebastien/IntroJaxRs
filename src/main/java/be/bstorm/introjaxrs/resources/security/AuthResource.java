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

@Path("/auth")
public class AuthResource {

    @Inject
    private AuthService authService;

    @Inject
    private Validator validator;

    @Inject
    private JwtUtils jwtUtils;

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
