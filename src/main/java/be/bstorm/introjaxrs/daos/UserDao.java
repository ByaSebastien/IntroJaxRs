package be.bstorm.introjaxrs.daos;

import be.bstorm.introjaxrs.pojos.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

@ApplicationScoped
public class UserDao extends CrudDao<User, Integer> {

    public Optional<User> findByEmailOrUsername(String login) {
        try(EntityManager em = emf.createEntityManager()){
            User user = em.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :login OR u.username = :login", User.class)
                    .setParameter("login", login)
                    .getSingleResult();

            return Optional.ofNullable(user);
        }
    }

    public boolean existsByEmail(String email) {
        try(EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult() > 0;
        }
    }

    public boolean existsByUsername(String username) {
        try(EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                    .setParameter("username", username)
                    .getSingleResult() > 0;
        }
    }
}
