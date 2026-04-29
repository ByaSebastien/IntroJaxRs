package be.bstorm.introjaxrs.enums;

/**
 * Énumération des statuts possibles d'une commande.
 *
 * Représente l'état de progression d'une commande tout au long de son cycle de vie :
 * <ul>
 *   <li>{@link #PENDING} - Commande en attente de traitement</li>
 *   <li>{@link #SEND} - Commande envoyée/expédiée</li>
 *   <li>{@link #CANCELED} - Commande annulée</li>
 *   <li>{@link #WAITING} - Commande en attente (stock insuffisant)</li>
 * </ul>
 *
 * @author IntroJaxRs
 * @version 1.0
 */
public enum OrderStatus {
    /**
     * Commande en attente de traitement
     */
    PENDING,

    /**
     * Commande envoyée/expédiée
     */
    SEND,

    /**
     * Commande annulée
     */
    CANCELED,

    /**
     * Commande en attente (généralement due au stock insuffisant)
     */
    WAITING,
}
