package be.bstorm.introjaxrs.services.security;

import at.favre.lib.crypto.bcrypt.BCrypt;
import be.bstorm.introjaxrs.daos.UserDao;
import be.bstorm.introjaxrs.models.security.auth.LoginRequest;
import be.bstorm.introjaxrs.models.security.auth.RegisterRequest;
import be.bstorm.introjaxrs.pojos.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthService {

    @Inject
    private UserDao userDao;

    public void register(RegisterRequest request) {

        if(userDao.existsByEmail(request.email()))
            throw new RuntimeException("Email already exists");
        if(userDao.existsByUsername(request.username()))
            throw new RuntimeException("Username already exists");

        User user = request.toUser();
        user.setPassword(BCrypt.withDefaults().hashToString(12, request.password().toCharArray()));
        //todo set default role
        userDao.save(user);
    }

    public User login(LoginRequest request) {

        User user = userDao.findByEmailOrUsername(request.login())
                .orElseThrow(() -> new RuntimeException("Invalid email/username or password"));

        if(!BCrypt.verifyer().verify(request.password().toCharArray(), user.getPassword()).verified)
            throw new RuntimeException("Invalid email/username or password");

        return user;
    }
}
