package be.bstorm.introjaxrs.pojos;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.util.UUID;

/**
 * Entité JPA représentant un produit du catalogue.
 *
 * Un produit contient les détails commerciaux ainsi que les associations avec
 * une catégorie et un stock. Chaque produit est identifié par un UUID unique.
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@Entity
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
public class Product {

    /**
     * Identifiant unique du produit (UUID)
     */
    @Id
    @Getter @Setter
    private UUID id;

    /**
     * Nom unique du produit (max 150 caractères)
     */
    @Getter @Setter
    @Column(length = 150, nullable = false, unique = true)
    private String name;

    /**
     * Marque/fabricant du produit (max 50 caractères)
     */
    @Getter @Setter
    @Column(length = 50, nullable = false)
    private String brand;

    /**
     * Prix du produit en centimes (valeur entière positive)
     */
    @Range(max = Integer.MAX_VALUE)
    @Getter @Setter
    @Column(nullable = false)
    private int price;

    /**
     * Description détaillée du produit (max 500 caractères)
     */
    @Getter @Setter
    @Column(length = 500)
    private String description;

    /**
     * URL ou chemin de l'image du produit
     */
    @Getter @Setter
    private String image;

    /**
     * Catégorie à laquelle appartient le produit (relation many-to-one)
     */
    @Getter @Setter
    @ManyToOne(
            optional = false,
            cascade = {CascadeType.PERSIST,CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private Category category;

    /**
     * Stock disponible du produit (relation one-to-one)
     */
    @Getter @Setter
    @OneToOne(
            optional = false,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private Stock stock;
}
