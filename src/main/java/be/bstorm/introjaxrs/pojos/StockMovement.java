package be.bstorm.introjaxrs.pojos;

import be.bstorm.introjaxrs.enums.MovementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

/**
 * Entité JPA représentant un mouvement de stock.
 *
 * Enregistre chaque modification du stock d'un produit (entrée ou sortie).
 * Permet une traçabilité complète des variations de quantité.
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@Entity
@NoArgsConstructor @AllArgsConstructor
public class StockMovement {

    /**
     * Identifiant unique générée automatiquement du mouvement
     */
    @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Quantité impliquée dans ce mouvement (valeur positive)
     */
    @Range(max = Integer.MAX_VALUE)
    @Getter @Setter
    @Column(nullable = false)
    private int quantity;

    /**
     * Type de mouvement : IN (entrée) ou OUT (sortie)
     */
    @Getter @Setter
    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private MovementType type;

    /**
     * Produit concerné par ce mouvement de stock
     */
    @Getter @Setter
    @ManyToOne(optional = false)
    private Product product;
}
