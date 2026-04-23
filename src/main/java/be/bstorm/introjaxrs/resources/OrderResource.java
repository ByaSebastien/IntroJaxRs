package be.bstorm.introjaxrs.resources;

import be.bstorm.introjaxrs.daos.OrderDao;
import be.bstorm.introjaxrs.daos.OrderLineDao;
import be.bstorm.introjaxrs.daos.ProductDao;
import be.bstorm.introjaxrs.daos.UserDao;
import be.bstorm.introjaxrs.enums.OrderStatus;
import be.bstorm.introjaxrs.models.order.OrderLineRequest;
import be.bstorm.introjaxrs.models.order.OrderRequest;
import be.bstorm.introjaxrs.pojos.Order;
import be.bstorm.introjaxrs.pojos.OrderLine;
import be.bstorm.introjaxrs.pojos.Product;
import be.bstorm.introjaxrs.pojos.User;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/order")
public class OrderResource {

    @Inject
    private OrderDao orderDao;

    @Inject
    private OrderLineDao orderLineDao;

    @Inject
    ProductDao productDao;

    @Inject
    UserDao userDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(OrderRequest orderRequest) {

        List<UUID> requestedProductIds = orderRequest.orderLines().stream()
                .map(OrderLineRequest::productId)
                .toList();

        Map<UUID, Product> productMap = productDao.findByIdsWithStock(requestedProductIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<String> errors = new ArrayList<>();

        for (OrderLineRequest orderLine : orderRequest.orderLines()) {
            Product product = productMap.get(orderLine.productId());

            if (product == null) {
                errors.add("Produit avec l'ID " + orderLine.productId() + " n'existe pas");
                continue;
            }

            if (product.getStock().getQuantity() < orderLine.quantity()) {
                errors.add("Stock insuffisant pour " + product.getName() +
                        " (demandé: " + orderLine.quantity() +
                        ", disponible: " + product.getStock().getQuantity() + ")");
            }
        }

        if (!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("errors", errors))
                    .build();
        }

        // Todo get authenticated user
        // Simulation d'un utilisateur connecté
        User user = userDao.findById(1).orElseThrow();

        Order order = new Order(
                UUID.randomUUID(),
                LocalDateTime.now(),
                OrderStatus.PENDING,
                user
        );

        orderDao.save(order);

        List<OrderLine> orderLines = orderRequest.orderLines().stream()
                .map(ol -> {
                    Product product = productMap.get(ol.productId());
                    return new OrderLine(
                            ol.quantity(),
                            product.getPrice(),
                            order,
                            product
                    );
                })
                .toList();

        orderLineDao.saveAll(orderLines);

        return Response.status(Response.Status.CREATED)
                .build();
    }
}
