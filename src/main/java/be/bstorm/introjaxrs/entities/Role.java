package be.bstorm.introjaxrs.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_")
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
public class Role {

    @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter @Setter
    @Column(length = 50, nullable = false, unique = true)
    private String name;
}
