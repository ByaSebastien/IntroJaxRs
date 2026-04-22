package be.bstorm.introjaxrs.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.util.UUID;

@Entity
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
public class Product {

    @Id
    @Getter @Setter
    private UUID id;

    @Getter @Setter
    @Column(length = 150, nullable = false, unique = true)
    private String name;

    @Range(max = Integer.MAX_VALUE)
    @Getter @Setter
    @Column(nullable = false)
    private int price;
}
