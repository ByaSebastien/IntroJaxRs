package be.bstorm.introjaxrs.services;

import be.bstorm.introjaxrs.daos.*;
import be.bstorm.introjaxrs.enums.OrderStatus;
import be.bstorm.introjaxrs.models.order.OrderLineRequest;
import be.bstorm.introjaxrs.models.order.OrderRequest;
import be.bstorm.introjaxrs.models.order.ValidateOrderRequest;
import be.bstorm.introjaxrs.pojos.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service métier pour la gestion des commandes.
 *
 * Gère le cycle de vie des commandes incluant:
 * <ul>
 *   <li>Création de nouvelles commandes</li>
 *   <li>Validation et traitement des commandes</li>
 *   <li>Gestion automatique des réapprovisionnements lorsque le stock est faible</li>
 * </ul>
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@ApplicationScoped
public class OrderService {

    /**
     * DAO pour les opérations sur les commandes
     */
    @Inject
    private OrderDao orderDao;

    /**
     * DAO pour les opérations sur les lignes de commande
     */
    @Inject
    private OrderLineDao orderLineDao;

    /**
     * DAO pour les opérations sur les produits
     */
    @Inject
    ProductDao productDao;

    /**
     * DAO pour les opérations sur le stock
     */
    @Inject
    StockDao stockDao;

    /**
     * Crée et sauvegarde une nouvelle commande.
     *
     * Effectue les vérifications suivantes:
     * <ul>
     *   <li>Tous les produits demandés existent</li>
     *   <li>Le stock suffisant est disponible pour chaque produit</li>
     * </ul>
     *
     * @param order la requête de commande contenant les lignes
     * @param connectedUser l'utilisateur qui place la commande
     * @throws RuntimeException si les produits n'existent pas ou le stock est insuffisant
     *
     * @see #handleOrderErrors(OrderRequest, Map)
     */
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

    /**
     * Valide et traite une commande en attente.
     *
     * Gère trois scénarios:
     * <ul>
     *   <li>Commande complète: décrément du stock et envoi</li>
     *   <li>Commande incomplète: création d'une commande de remplissage</li>
     *   <li>Réapprovisionnement: création automatique d'une commande d'achat</li>
     * </ul>
     *
     * @param id l'UUID de la commande à valider
     * @param validateOrderRequest les lignes complètes et/ou incomplètes
     * @throws RuntimeException si la commande n'existe pas ou les produits n'existent pas
     *
     * @see OrderStatus
     */
    public void validate(UUID id, ValidateOrderRequest validateOrderRequest) {

        Order order = orderDao.findByIdWithUser(id).orElseThrow(() -> new RuntimeException("Order not found"));
        List<OrderLine> lines = orderLineDao.findByOrderId(order.getId());

        List<Product> products = productDao.findByIdsWithStock(
                lines.stream()
                        .map(ol -> ol.getProduct().getId())
                        .toList()
        );

        Map<UUID, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        List<OrderLine> triggerOrderLines = new ArrayList<>();

        if(validateOrderRequest.incomplete() == null) {

            List<OrderLineRequest> validLines = validateOrderRequest.complete().orderLines();

            List<String> errors = new ArrayList<>();

            for(OrderLineRequest ol : validLines) {
                Product product = productMap.get(ol.productId());
                if (product == null) {
                    errors.add("Product " + ol.productId() + " not found");
                    continue;
                }
                product.getStock().setQuantity(product.getStock().getQuantity() - ol.quantity());
                if(product.getStock().getQuantity() <= product.getStock().getThreshold()) {
                    triggerOrderLines.add(new OrderLine(
                            ol.quantity(),
                            product.getPrice(),
                            product
                    ));
                }
            }

            if(!errors.isEmpty()) {
                throw new RuntimeException(String.join("; ", errors));
            }

            order.setOrderStatus(OrderStatus.SEND);
            orderDao.update(order);
            return;
        }

        order.setOrderStatus(OrderStatus.CANCELED);
        orderDao.update(order);

        if(validateOrderRequest.complete() != null) {

            Order completedOrder = new Order(
                    UUID.randomUUID(),
                    LocalDateTime.now(),
                    OrderStatus.SEND,
                    order.getUser(),
                    order
            );

            completedOrder = orderDao.save(completedOrder);

            List<OrderLine> completedOrderLines = new ArrayList<>();

            List<String> errors = new ArrayList<>();

            for(OrderLineRequest ol : validateOrderRequest.complete().orderLines()) {
                Product product = productMap.get(ol.productId());
                if(product == null) {
                    errors.add("Product " + ol.productId() + " not found");
                    continue;
                }

                completedOrderLines.add(new OrderLine(
                        ol.quantity(),
                        product.getPrice(),
                        completedOrder,
                        product
                ));

                Stock stock = productMap.get(ol.productId()).getStock();
                stock.setQuantity(stock.getQuantity() - ol.quantity());
                stockDao.update(stock);

                if(stock.getQuantity() <= stock.getThreshold()) {
                    triggerOrderLines.add(new OrderLine(
                            ol.quantity(),
                            product.getPrice(),
                            product
                    ));
                }
            }

            if(!errors.isEmpty()) {
                throw new RuntimeException(String.join("; ", errors));
            }

            orderLineDao.saveAll(completedOrderLines);

            if(!triggerOrderLines.isEmpty()) {

                Order triggerOrder = new Order(
                        UUID.randomUUID(),
                        LocalDateTime.now(),
                        OrderStatus.WAITING,
                        order.getUser(),
                        order
                );

                triggerOrder = orderDao.save(triggerOrder);

                for(OrderLine ol  : triggerOrderLines) {
                    ol.setOrder(triggerOrder);
                }

                orderLineDao.saveAll(triggerOrderLines);
            }
        }

        Order newOrder = new Order(
                UUID.randomUUID(),
                LocalDateTime.now(),
                OrderStatus.WAITING,
                order.getUser(),
                order
        );

        newOrder = orderDao.save(newOrder);

        List<OrderLine> newOrderLines = new ArrayList<>();

        List<String> errors = new ArrayList<>();

        for(OrderLineRequest ol : validateOrderRequest.incomplete().orderLines()) {
            Product product = productMap.get(ol.productId());
            if(product == null) {
                errors.add("Product " + ol.productId() + " not found");
                continue;
            }

            newOrderLines.add(new OrderLine(
                    ol.quantity(),
                    product.getPrice(),
                    newOrder,
                    product
            ));
        }

        if(!errors.isEmpty()) {
            throw new RuntimeException(String.join("; ", errors));
        }

        orderLineDao.saveAll(newOrderLines);
    }

    /**
     * Crée et sauvegarde une nouvelle commande en statut PENDING.
     *
     * @param connectedUser l'utilisateur qui place la commande
     * @return la commande nouvellement créée
     */
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

    /**
     * Récupère une map des produits demandés indexée par UUID.
     *
     * @param order la requête de commande
     * @return une map UUID -> Product
     */
    private Map<UUID, Product> getProductMap(OrderRequest order) {
        List<UUID> requestedProductIds = order.orderLines().stream()
                .map(OrderLineRequest::productId)
                .toList();

        Map<UUID, Product> productMap = productDao.findByIdsWithStock(requestedProductIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        return productMap;
    }

    /**
     * Construit une liste de lignes de commande à partir de la requête.
     *
     * @param order la requête de commande
     * @param productMap la map des produits disponibles
     * @param newOrder la commande à laquelle appartiennent les lignes
     * @return la liste des lignes de commande
     */
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

    /**
     * Valide les erreurs dans une requête de commande.
     *
     * Vérifie:
     * <ul>
     *   <li>L'existence de chaque produit demandé</li>
     *   <li>La disponibilité de stock suffisant</li>
     * </ul>
     *
     * @param order la requête de commande
     * @param productMap la map des produits avec stock
     * @return une liste des messages d'erreur (vide si pas d'erreur)
     */
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

