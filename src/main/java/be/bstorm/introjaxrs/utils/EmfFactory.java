package be.bstorm.introjaxrs.utils;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Factory pour obtenir l'EntityManagerFactory Hibernate.
 *
 * Utilise le pattern Singleton pour assurer qu'une seule instance
 * du factory soit créée pour toute l'application.
 *
 * @author IntroJaxRs
 * @version 1.0
 */
public class EmfFactory {

        /**
         * Nom de l'unité de persistance définie dans persistence.xml
         */
        private static final String PERSISTENCE_UNIT_NAME = "IntroJaxRs";

        /**
         * Instance unique de l'EntityManagerFactory
         */
        private static EntityManagerFactory emf;

        /**
         * Obtient l'EntityManagerFactory, le créant si nécessaire.
         *
         * @return l'instance de EntityManagerFactory
         */
        public static EntityManagerFactory getEmf() {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            }
            return emf;
        }
}
