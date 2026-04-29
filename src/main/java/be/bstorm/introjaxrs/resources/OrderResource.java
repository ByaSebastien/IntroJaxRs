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

/**
 * Ressource JAX-RS pour la gestion des commandes.
 *
 * Fournit des endpoints pour créer et valider des commandes.
 * Endpoints sécurisés nécessitant l'authentification et les rôles appropriés.
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@Path("/order")
public class OrderResource {

    /**
     * DAO pour les opérations sur les utilisateurs
     * TODO: remplacer par la récupération de l'utilisateur authentifié
     */
    @Inject
    UserDao userDao;

    /**
     * Service métier pour les commandes
     */
    @Inject
    OrderService orderService;

    /**
     * Validateur Jakarta pour les contraintes de validation
     */
    @Inject
    Validator validator;

    /**
     * Crée une nouvelle commande.
     *
     * Nécessite le rôle USER.
     * Valide les données de la requête avant de créer la commande.
     *
     * @param orderRequest la requête de commande avec les articles
     * @return une réponse 201 CREATED en cas de succès
     *
     * @response 201 Commande créée avec succès
     * @response 400 Erreur de validation ou stock insuffisant
     * @response 403 Utilisateur non authentifié ou sans le rôle USER
     *
     * @throws RuntimeException si les produits n'existent pas ou le stock est insuffisant
     */
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

    /**
     * Valide et traite une commande en attente.
     *
     * Nécessite le rôle MANDAY (responsable de la validation).
     * Gère les commandes complètes, incomplètes et les réapprovisionnements.
     *
     * @param id l'UUID de la commande à valider
     * @param validateOrderRequest contient les articles complétés et/ou non livrables
     * @return une réponse 202 ACCEPTED en cas de succès
     *
     * @response 202 Commande validée avec succès
     * @response 400 Erreur de validation ou données invalides
     * @response 403 Utilisateur non autorisé
     * @response 404 Commande non trouvée
     */
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
