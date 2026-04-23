package be.bstorm.introjaxrs.pojos;

import jakarta.persistence.*;
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

    @Getter @Setter
    @Column(length = 50, nullable = false)
    private String brand;

    @Range(max = Integer.MAX_VALUE)
    @Getter @Setter
    @Column(nullable = false)
    private int price;

    @Getter @Setter
    @Column(length = 500)
    private String description;

    @Getter @Setter
    private String image;

    @Getter @Setter
    @ManyToOne(
            optional = false,
            cascade = {CascadeType.PERSIST,CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private Category category;
}
