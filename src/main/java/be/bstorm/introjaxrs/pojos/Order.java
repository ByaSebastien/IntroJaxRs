package be.bstorm.introjaxrs.pojos;

import be.bstorm.introjaxrs.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "order_")
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
public class Order {

    @Id
    @Getter @Setter
    private UUID id;

    @Getter @Setter
    private LocalDateTime orderDate;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Getter @Setter
    @ManyToOne(optional = false, cascade = {CascadeType.MERGE})
    private User user;

    @Getter @Setter
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Order baseOrder;

    public Order(UUID id, LocalDateTime orderDate, OrderStatus orderStatus, User user) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.user = user;
    }
}
