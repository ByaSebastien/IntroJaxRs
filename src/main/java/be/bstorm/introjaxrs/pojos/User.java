package be.bstorm.introjaxrs.pojos;

import jakarta.persistence.*;
import lombok.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité JPA représentant un utilisateur de l'application.
 *
 * Chaque utilisateur possède des credentials (email et nom d'utilisateur uniques),
 * un mot de passe hashé et une collection de rôles définissant ses permissions.
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@Entity
@Table(name = "user_")
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
public class User {

    /**
     * Identifiant unique générée automatiquement de l'utilisateur
     */
    @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Email unique de l'utilisateur (max 150 caractères)
     */
    @Getter
    @Column(length = 150, nullable = false, unique = true)
    private String email;

    /**
     * Nom d'utilisateur unique (max 50 caractères)
     */
    @Getter
    @Column(length = 50, nullable = false, unique = true)
    private String username;

    /**
     * Mot de passe hashé de l'utilisateur (algorithme BCrypt)
     */
    @Getter
    @Column(nullable = false)
    private String password;

    /**
     * Ensemble des rôles assignés à cet utilisateur
     * (relation many-to-many)
     */
    @Getter
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private Set<Role> roles = new HashSet<>();

    /**
     * Constructeur avec paramètres pour créer un utilisateur complet.
     *
     * @param email l'email unique de l'utilisateur
     * @param username le nom d'utilisateur unique
     * @param password le mot de passe (à hasher avant stockage)
     */
    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    /**
     * Ajoute un rôle à cet utilisateur.
     *
     * @param role le rôle à ajouter
     */
    public void addRole(Role role){
        this.roles.add(role);
    }

    /**
     * Supprime un rôle de cet utilisateur.
     *
     * @param role le rôle à supprimer
     */
    public void removeRole(Role role){
        this.roles.remove(role);
    }

    /**
     * Définit l'email de l'utilisateur et retourne cette instance pour le chaînage.
     *
     * @param email le nouvel email
     * @return cette instance User
     */
    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Définit le nom d'utilisateur et retourne cette instance pour le chaînage.
     *
     * @param username le nouveau nom d'utilisateur
     * @return cette instance User
     */
    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * Définit le mot de passe et retourne cette instance pour le chaînage.
     *
     * @param password le nouveau mot de passe
     * @return cette instance User
     */
    public User setPassword(String password) {
        this.password = password;
        return this;
    }
}
