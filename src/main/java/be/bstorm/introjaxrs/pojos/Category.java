package be.bstorm.introjaxrs.pojos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
public class Category {

    @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter @Setter
    @Column(length = 150, nullable = false, unique = true)
    private String name;
}
