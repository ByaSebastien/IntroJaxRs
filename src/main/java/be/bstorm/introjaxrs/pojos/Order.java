package be.bstorm.introjaxrs.pojos;

import be.bstorm.introjaxrs.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Entité JPA représentant une commande.
 *
 * Une commande est créée par un utilisateur, contient un statut et peut être
 * basée sur une commande antérieure. Elle est identifiée par un UUID unique.
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@Entity
@Table(name = "order_")
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
public class Order {

    /**
     * Identifiant unique (UUID) de la commande
     */
    @Id
    @Getter @Setter
    private UUID id;

    /**
     * Date et heure de création de la commande
     */
    @Getter @Setter
    private LocalDateTime orderDate;

    /**
     * Statut actuel de la commande
     */
    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    /**
     * Utilisateur qui a créé cette commande
     * (relation many-to-one obligatoire)
     */
    @Getter @Setter
    @ManyToOne(optional = false, cascade = {CascadeType.MERGE})
    private User user;

    /**
     * Commande parent si cette commande est une modification/reconduction
     * d'une commande antérieure (relation many-to-one optionnelle)
     */
    @Getter @Setter
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Order baseOrder;

    /**
     * Constructeur simplifié pour créer une commande sans commande parent.
     *
     * @param id l'UUID de la commande
     * @param orderDate la date de la commande
     * @param orderStatus le statut initial
     * @param user l'utilisateur
     */
    public Order(UUID id, LocalDateTime orderDate, OrderStatus orderStatus, User user) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.user = user;
    }
}
