package be.bstorm.introjaxrs.pojos;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Entité JPA représentant une ligne d'une commande.
 *
 * Une ligne de commande est la connexion entre une commande et un produit,
 * spécifiant la quantité et le prix unitaire au moment de la commande.
 *
 * Utilise une clé primaire composite (@EmbeddedId) composée
 * de l'identifiant de la commande et de l'identifiant du produit.
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@Entity
@NoArgsConstructor
@EqualsAndHashCode @ToString
public class OrderLine {

    /**
     * Clé primaire composite contenant l'ID de la commande et du produit
     */
    @EmbeddedId
    @Getter
    private OrderLineId id = new OrderLineId();

    /**
     * Quantité du produit commandée
     */
    @Getter @Setter
    private int quantity;

    /**
     * Prix unitaire du produit au moment de la commande (en centimes)
     */
    @Getter @Setter
    private int price;

    /**
     * Commande à laquelle appartient cette ligne
     * (relation many-to-one obligatoire)
     */
    @Getter
    @ManyToOne(optional = false, cascade = {CascadeType.MERGE})
    @MapsId("orderId")
    private Order order;

    /**
     * Produit commandé dans cette ligne
     * (relation many-to-one obligatoire)
     */
    @Getter
    @ManyToOne(optional = false, cascade = {CascadeType.MERGE})
    @MapsId("productId")
    private Product product;

    /**
     * Définit la commande pour cette ligne et met à jour l'ID composite.
     *
     * @param order la commande
     */
    public void setOrder(Order order) {
        this.order = order;
        this.id.setOrderId(order.getId());
    }


    /**
     * Définit le produit pour cette ligne et met à jour l'ID composite.
     *
     * @param product le produit
     */
    public void setProduct(Product product) {
        this.product = product;
        this.id.setProductId(product.getId());
    }

    /**
     * Constructeur avec quantité, prix et produit.
     *
     * @param quantity la quantité
     * @param price le prix unitaire
     * @param product le produit
     */
    public OrderLine(int quantity, int price, Product product) {
        this.quantity = quantity;
        this.price = price;
        this.product = product;
    }

    /**
     * Constructeur complet avec tous les paramètres.
     *
     * @param quantity la quantité
     * @param price le prix unitaire
     * @param order la commande
     * @param product le produit
     */
    public OrderLine(int quantity, int price, Order order, Product product) {
        this(quantity, price, product);
        this.id = new OrderLineId(order.getId(), product.getId());
        this.order = order;
    }

    /**
     * Classe embeddable représentant la clé primaire composite d'une ligne de commande.
     *
     * Combine l'ID de la commande et l'ID du produit pour former une clé unique.
     *
     * @author IntroJaxRs
     * @version 1.0
     */
    @Embeddable
    @NoArgsConstructor @AllArgsConstructor
    @EqualsAndHashCode @ToString
    public static class OrderLineId {

        /**
         * Identifiant de la commande (UUID)
         */
        @Getter @Setter
        private UUID orderId;

        /**
         * Identifiant du produit (UUID)
         */
        @Getter @Setter
        private UUID productId;
    }
}

// alternative plus digeste
//@Entity
//@Table(@UniqueConstraint(columnNames = {"order_id", "product_id"}))
//public class OrderLine {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Getter
//    private Integer id;
//
//    @Getter @Setter
//    private int quantity;
//
//    @Getter @Setter
//    private int price;
//
//    @Getter @Setter
//    @ManyToOne(optional = false)
//    private Order order;
//
//    @Getter @Setter
//    @ManyToOne(optional = false)
//    private Product product;
//}

