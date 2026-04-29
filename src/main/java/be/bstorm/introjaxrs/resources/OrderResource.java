package be.bstorm.introjaxrs.resources;

import be.bstorm.introjaxrs.annotations.security.HasAuthority;
import be.bstorm.introjaxrs.daos.UserDao;
import be.bstorm.introjaxrs.models.order.OrderRequest;
import be.bstorm.introjaxrs.models.order.ValidateOrderRequest;
import be.bstorm.introjaxrs.pojos.User;
import be.bstorm.introjaxrs.services.OrderService;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Path("/order")
public class OrderResource {

    // pour simuler authUser
    @Inject
    UserDao userDao;

    @Inject
    OrderService orderService;

    @Inject
    Validator validator;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @HasAuthority(roles = "USER")
    public Response post(OrderRequest orderRequest) {

        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);
        if (!violations.isEmpty()) {
            List<String> errors = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .toList();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("errors", errors))
                    .build();
        }

        // Todo get authenticated user
        // Simulation d'un utilisateur connecté
        User user = userDao.findById(1).orElseThrow();

        try {
            orderService.postOrder(orderRequest, user);

            return Response.status(Response.Status.CREATED)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @PATCH
    @Path("/validate/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @HasAuthority(roles = "MANDAY")
    public Response validate(
            @PathParam("id") UUID id,
            ValidateOrderRequest validateOrderRequest
    ) {

        Set<ConstraintViolation<ValidateOrderRequest>> violations = validator.validate(validateOrderRequest);
        if (!violations.isEmpty()) {
            List<String> errors = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .toList();

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("errors", errors))
                    .build();
        }

        try{
            orderService.validate(id,validateOrderRequest);
            return Response.status(Response.Status.ACCEPTED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
















}
