package be.bstorm.introjaxrs.enums;

/**
 * Énumération des types de mouvements de stock.
 *
 * Represents les deux types de mouvements de stock possibles :
 * <ul>
 *   <li>{@link #IN} - Entrée de stock (approvisionnement)</li>
 *   <li>{@link #OUT} - Sortie de stock (vente/retrait)</li>
 * </ul>
 *
 * @author IntroJaxRs
 * @version 1.0
 */
public enum MovementType {
    /**
     * Entrée de stock - approvisionnement ou retour de marchandise
     */
    IN,

    /**
     * Sortie de stock - vente ou prélèvement
     */
    OUT
}
