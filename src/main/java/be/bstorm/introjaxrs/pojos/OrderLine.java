package be.bstorm.introjaxrs.pojos;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@EqualsAndHashCode @ToString
public class OrderLine {

    @EmbeddedId
    @Getter
    private OrderLineId id = new OrderLineId();

    @Getter @Setter
    private int quantity;

    @Getter @Setter
    private int price;

    @Getter
    @ManyToOne(optional = false)
    @MapsId("orderId")
    private Order order;

    @Getter
    @ManyToOne(optional = false)
    @MapsId("productId")
    private Product product;

    public void setOrder(Order order) {
        this.order = order;
        this.id.setOrderId(order.getId());
    }


    public void setProduct(Product product) {
        this.product = product;
        this.id.setProductId(product.getId());
    }

    @Embeddable
    @NoArgsConstructor @AllArgsConstructor
    @EqualsAndHashCode @ToString
    private static class OrderLineId {

        @Getter @Setter
        private UUID orderId;

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
