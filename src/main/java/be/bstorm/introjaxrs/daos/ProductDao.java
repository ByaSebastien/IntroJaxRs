package be.bstorm.introjaxrs.daos;

import be.bstorm.introjaxrs.pojos.Product;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class ProductDao extends CrudDao<Product, UUID> {
}
