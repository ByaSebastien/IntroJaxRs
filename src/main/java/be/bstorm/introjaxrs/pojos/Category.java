package be.bstorm.introjaxrs.pojos;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité JPA représentant une catégorie de produit.
 *
 * Les catégories sont utilisées pour classifier les produits disponibles dans le système.
 * Chaque catégorie possède un identifiant unique et un nom unique.
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@Entity
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
public class Category {

    /**
     * Identifiant unique générée automatiquement de la catégorie
     */
    @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nom unique de la catégorie (max 150 caractères)
     */
    @Getter @Setter
    @Column(length = 150, nullable = false, unique = true)
    private String name;
}
