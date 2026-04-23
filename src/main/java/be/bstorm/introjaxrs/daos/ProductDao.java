package be.bstorm.introjaxrs.daos;

import be.bstorm.introjaxrs.pojos.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ProductDao extends CrudDao<Product, UUID> {

    public List<Product> findAllWithCategory() {
        try(EntityManager em =  emf.createEntityManager()) {

            return em.createQuery(
                    "SELECT p FROM Product p JOIN FETCH p.category",
                    Product.class).getResultList();
        }
    }

    public Optional<Product> findByIdWithCategoryAndStock(UUID id) {
        try(EntityManager em =  emf.createEntityManager()) {

            return em.createQuery(
                    "SELECT p FROM Product p JOIN FETCH p.category JOIN FETCH p.stock WHERE p.id = :id",
                    Product.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst();
        }
    }

    public List<Product> findByIdsWithStock(List<UUID> ids) {
        try(EntityManager em =  emf.createEntityManager()) {
            return em.createQuery(
                    "SELECT p FROM Product p JOIN FETCH p.stock s WHERE p.id IN :ids",
                    Product.class)
                    .setParameter("ids", ids)
                    .getResultList();
        }
    }
}
