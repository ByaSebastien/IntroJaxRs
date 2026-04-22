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
    private int quantity;

    @Range
    @Getter @Setter
    private int threshold;

    @Getter @Setter
    @OneToOne(optional = false)
    private Product product;
}
