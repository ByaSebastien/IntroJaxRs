package be.bstorm.introjaxrs.pojos;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

/**
 * Entité JPA représentant le stock d'un produit.
 *
 * Gère la quantité disponible, le seuil d'alerte et la quantité
 * à commander automatiquement lorsque le stock descend en dessous du seuil.
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@Entity
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
public class Stock {

    /**
     * Identifiant unique générée automatiquement du stock
     */
    @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Quantité actuelle en stock (valeur non négative, défaut: 0)
     */
    @Getter @Setter
    @Column(nullable = false)
    private int quantity = 0;

    /**
     * Seuil minimum d'alerte pour automatiser les réapprovionnements
     */
    @Range
    @Getter @Setter
    private int threshold;

    /**
     * Quantité standard à commander lors d'un réapprovisionnement automatique
     */
    @Range
    @Getter @Setter
    private int orderQuantity;
}
