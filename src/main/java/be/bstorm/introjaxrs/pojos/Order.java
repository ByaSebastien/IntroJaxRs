package be.bstorm.introjaxrs.pojos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    @ManyToOne(optional = false)
    private User user;
}
