package be.bstorm.introjaxrs.utils;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EmfFactory {

        private static final String PERSISTENCE_UNIT_NAME = "IntroJaxRs";

        private static EntityManagerFactory emf;

        public static EntityManagerFactory getEmf() {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            }
            return emf;
        }
}
