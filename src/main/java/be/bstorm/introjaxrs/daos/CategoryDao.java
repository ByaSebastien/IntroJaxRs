package be.bstorm.introjaxrs.daos;

import be.bstorm.introjaxrs.pojos.Category;
import be.bstorm.introjaxrs.utils.EmfFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CategoryDao {

    private final EntityManagerFactory emf;

    public CategoryDao() {
        this.emf = EmfFactory.getEmf();
    }

    public List<Category> findAll(){

        try(EntityManager em = emf.createEntityManager()) {
            return em.createQuery("select c from Category c", Category.class).getResultList();
        }
    }

    public Optional<Category> findById(Integer id){

        try(EntityManager em = emf.createEntityManager()){
            Category category = em.find(Category.class,id);
            return Optional.ofNullable(category);
        }
    }

    public Category save(Category category) {

        try(EntityManager em = emf.createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            em.persist(category);
            transaction.commit();
            return category;
        }
    }

    public Category update(Category category) {

        try(EntityManager em = emf.createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            em.merge(category);
            transaction.commit();
            return category;
        }
    }

    public Category remove(Integer id){

        try(EntityManager em = emf.createEntityManager()){
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            Category category = em.getReference(Category.class,id);
            em.remove(category);
            transaction.commit();
            return category;
        }
    }
}
