package be.bstorm.introjaxrs.pojos;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Entity
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
public class Stock {

    @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter @Setter
    @Column(nullable = false)
    private int quantity = 0;

    @Range
    @Getter @Setter
    private int threshold;

    @Range
    @Getter @Setter
    private int orderQuantity;
}
