package be.bstorm.introjaxrs.resources;

import be.bstorm.introjaxrs.daos.UserDao;
import be.bstorm.introjaxrs.models.order.OrderRequest;
import be.bstorm.introjaxrs.pojos.User;
import be.bstorm.introjaxrs.services.OrderService;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
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
    public Response post(@Valid OrderRequest orderRequest) {

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
}
