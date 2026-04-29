package be.bstorm.introjaxrs.daos;

import be.bstorm.introjaxrs.pojos.Role;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoleDao extends CrudDao<Role, Integer> {
}
