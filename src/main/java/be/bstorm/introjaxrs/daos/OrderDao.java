package be.bstorm.introjaxrs.daos;

import be.bstorm.introjaxrs.pojos.Order;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class OrderDao extends CrudDao<Order, UUID> {
}
