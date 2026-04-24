package be.bstorm.introjaxrs.daos;

import be.bstorm.introjaxrs.pojos.Order;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class OrderDao extends CrudDao<Order, UUID> {

    public Optional<Order> findByIdWithUser(UUID orderId) {

        try(EntityManager em = emf.createEntityManager()){
            return em.createQuery(
                    "SELECT o FROM Order o " +
                            "JOIN FETCH o.user u " +
                            "WHERE o.id = :orderId", Order.class)
                    .setParameter("orderId", orderId)
                    .getResultStream()
                    .findFirst();
        }
    }
}
