package be.bstorm.introjaxrs.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
}
