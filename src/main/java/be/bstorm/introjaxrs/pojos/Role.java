package be.bstorm.introjaxrs.pojos;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité JPA représentant un rôle utilisateur.
 *
 * Les rôles sont utilisés pour contrôler les permissions et l'accessibilité
 * des ressources pour chaque utilisateur. Chaque rôle possède un identifiant unique
 * et un nom unique (ex: "USER", "ADMIN", "MANDAY").
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@Entity
@Table(name = "role_")
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
public class Role {

    /**
     * Identifiant unique générée automatiquement du rôle
     */
    @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nom unique du rôle (max 50 caractères)
     * Exemples: "USER", "ADMIN", "MANDAY"
     */
    @Getter @Setter
    @Column(length = 50, nullable = false, unique = true)
    private String name;

    /**
     * Constructeur avec paramètre pour créer un rôle avec un nom.
     *
     * @param name le nom du rôle
     */
    public Role(String name) {
        this.name = name;
    }
}
