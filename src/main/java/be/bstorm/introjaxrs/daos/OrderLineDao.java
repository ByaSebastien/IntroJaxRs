package be.bstorm.introjaxrs.daos;

import be.bstorm.introjaxrs.pojos.OrderLine;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrderLineDao extends CrudDao<OrderLine, OrderLine.OrderLineId> {

    public List<OrderLine> findByOrderId(UUID orderId){
        try(EntityManager em = emf.createEntityManager()){
            return em.createQuery(
                    "SELECT ol FROM OrderLine ol " +
                            "JOIN FETCH ol.order o " +
                            "WHERE o.id = :orderId", OrderLine.class)
                    .setParameter("orderId", orderId)
                    .getResultList();
        }
    }
}
