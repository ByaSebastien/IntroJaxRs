package be.bstorm.introjaxrs.services.security;

import at.favre.lib.crypto.bcrypt.BCrypt;
import be.bstorm.introjaxrs.daos.UserDao;
import be.bstorm.introjaxrs.models.security.auth.LoginRequest;
import be.bstorm.introjaxrs.models.security.auth.RegisterRequest;
import be.bstorm.introjaxrs.pojos.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Service d'authentification et d'autoritarisation.
 *
 * Gère l'enregistrement de nouveaux utilisateurs et l'authentification des utilisateurs.
 * Assure le hachage sécurisé des mots de passe à l'aide de BCrypt.
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@ApplicationScoped
public class AuthService {

    /**
     * DAO pour les opérations de base de données sur les utilisateurs
     */
    @Inject
    private UserDao userDao;

    /**
     * Enregistre un nouvel utilisateur dans l'application.
     *
     * Effectue les contrôles suivants:
     * <ul>
     *   <li>L'email n'existe pas déjà</li>
     *   <li>Le nom d'utilisateur n'existe pas déjà</li>
     * </ul>
     *
     * Le mot de passe est haché avec BCrypt avant stockage.
     *
     * @param request la requête d'enregistrement contenant email, username et password
     * @throws RuntimeException si l'email ou le nom d'utilisateur existent déjà
     */
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

    /**
     * Authentifie un utilisateur en vérifiant ses identifiants.
     *
     * Accepte soit l'email soit le nom d'utilisateur comme identifiant.
     * Le mot de passe fourni est vérifié contre le mot de passe haché stocké.
     *
     * @param request la requête de connexion avec login (email ou username) et password
     * @return l'utilisateur authentifié
     * @throws RuntimeException si l'email/nom d'utilisateur n'existe pas ou le mot de passe est incorrect
     */
    public User login(LoginRequest request) {

        User user = userDao.findByEmailOrUsername(request.login())
                .orElseThrow(() -> new RuntimeException("Invalid email/username or password"));

        if(!BCrypt.verifyer().verify(request.password().toCharArray(), user.getPassword()).verified)
            throw new RuntimeException("Invalid email/username or password");

        return user;
    }
}
