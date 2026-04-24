package be.bstorm.introjaxrs.daos;

import be.bstorm.introjaxrs.pojos.Stock;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StockDao extends CrudDao<Stock, Integer> {
}
