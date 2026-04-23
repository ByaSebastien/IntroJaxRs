package be.bstorm.introjaxrs.daos;

import be.bstorm.introjaxrs.pojos.User;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserDao extends CrudDao<User, Integer> {
}
