package be.bstorm.introjaxrs.services;

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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderService {

    @Inject
    private OrderDao orderDao;

    @Inject
    private OrderLineDao orderLineDao;

    @Inject
    ProductDao productDao;

    @Transactional
    public void postOrder(OrderRequest order, User connectedUser) {

        Map<UUID, Product> productMap = getProductMap(order);

        List<String> errors = handleOrderErrors(order, productMap);

        if(!errors.isEmpty()) {
            throw new RuntimeException(String.join("; ", errors));
        }

        Order newOrder = saveOrder(connectedUser);

        List<OrderLine> orderLines = buildOrderLine(order, productMap, newOrder);

        orderLineDao.saveAll(orderLines);
    }

    private Order saveOrder(User connectedUser) {
        Order newOrder = new Order(
                UUID.randomUUID(),
                LocalDateTime.now(),
                OrderStatus.PENDING,
                connectedUser
        );

        orderDao.save(newOrder);
        return newOrder;
    }

    private Map<UUID, Product> getProductMap(OrderRequest order) {
        List<UUID> requestedProductIds = order.orderLines().stream()
                .map(OrderLineRequest::productId)
                .toList();

        Map<UUID, Product> productMap = productDao.findByIdsWithStock(requestedProductIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        return productMap;
    }

    private static List<OrderLine> buildOrderLine(OrderRequest order, Map<UUID, Product> productMap, Order newOrder) {
        List<OrderLine> orderLines = order.orderLines().stream()
                .map(ol -> {
                    Product product = productMap.get(ol.productId());
                    return new OrderLine(
                            ol.quantity(),
                            product.getPrice(),
                            newOrder,
                            product
                    );
                })
                .toList();
        return orderLines;
    }

    private static List<String> handleOrderErrors(OrderRequest order, Map<UUID, Product> productMap) {
        List<String> errors = new ArrayList<>();

        for (OrderLineRequest orderLine : order.orderLines()) {
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
        return errors;
    }
}
