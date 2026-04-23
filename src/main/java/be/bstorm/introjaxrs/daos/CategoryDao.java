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
public class CategoryDao extends CrudDao<Category, Integer> {
}
