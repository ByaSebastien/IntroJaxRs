package be.bstorm.introjaxrs.entities;

import be.bstorm.introjaxrs.enums.MovementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Entity
@NoArgsConstructor @AllArgsConstructor
public class StockMovement {

    @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Range(max = Integer.MAX_VALUE)
    @Getter @Setter
    @Column(nullable = false)
    private int quantity;

    @Getter @Setter
    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private MovementType type;
}
