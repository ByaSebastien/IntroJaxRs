package be.bstorm.introjaxrs.daos;

import be.bstorm.introjaxrs.pojos.OrderLine;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrderLineDao extends CrudDao<OrderLine, OrderLine.OrderLineId> {
}
