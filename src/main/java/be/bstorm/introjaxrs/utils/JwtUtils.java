package be.bstorm.introjaxrs.utils;

import be.bstorm.introjaxrs.models.security.auth.UserResponse;
import be.bstorm.introjaxrs.pojos.Role;
import be.bstorm.introjaxrs.pojos.User;
import io.jsonwebtoken.*;
import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.List;

/**
 * Utilitaire pour la gestion des tokens JWT (JSON Web Token).
 *
 * Fournit des méthodes pour:
 * <ul>
 *   <li>Générer des tokens JWT pour les utilisateurs authentifiés</li>
 *   <li>Extraire les informations du token (ID, username, email, rôles)</li>
 *   <li>Valider l'intégrité et la validité des tokens</li>
 * </ul>
 *
 * Chaque token est valide pendant 15 minutes.
 *
 * @author IntroJaxRs
 * @version 1.0
 */
@ApplicationScoped
public class JwtUtils {

    /**
     * Builder JWT pour créer les tokens signés
     */
    private final JwtBuilder builder;

    /**
     * Parser JWT pour lire les tokens
     */
    private final JwtParser parser;

    /**
     * Constructeur initialisant le builder et le parser avec la clé secrète.
     */
    public JwtUtils() {
        this.builder = Jwts.builder().signWith(getSecretKey());
        this.parser = Jwts.parser().verifyWith(getSecretKey()).build();
    }

    /**
     * Génère un JWT signé pour un utilisateur.
     *
     * Le token contient:
     * <ul>
     *   <li>ID utilisateur</li>
     *   <li>Username</li>
     *   <li>Email</li>
     *   <li>Liste des rôles</li>
     *   <li>Timestamps d'émission et d'expiration</li>
     * </ul>
     *
     * @param user l'utilisateur pour lequel générer le token
     * @return le JWT signé sous forme de string
     */
    public String generateToken(User user) {
        long expiration = 900_000; // 15 minutes
        return builder
                .id(user.getId().toString())
                .subject(user.getUsername())
                .claim("email", user.getEmail())
                .claim("roles", user.getRoles().stream().map(Role::getName).toList())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

    /**
     * Extrait l'ID utilisateur du token.
     *
     * @param token le JWT
     * @return l'ID de l'utilisateur
     */
    public int getId(String token) {
        return Integer.parseInt(getClaims(token).getId());
    }

    /**
     * Extrait le nom d'utilisateur du token.
     *
     * @param token le JWT
     * @return le username
     */
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extrait l'email de l'utilisateur du token.
     *
     * @param token le JWT
     * @return l'email
     */
    public String getEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    /**
     * Extrait la liste des rôles de l'utilisateur du token.
     *
     * @param token le JWT
     * @return la liste des noms de rôles
     */
    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        return (List<String>) getClaims(token).get("roles", List.class);
    }

    /**
     * Valide la signature et la date d'expiration du token.
     *
     * @param token le JWT à valider
     * @return true si le token est valide, false sinon
     */
    public boolean isValid(String token){
        Date now = new Date();
        return getClaims(token).getIssuedAt().before(now) && getClaims(token).getExpiration().after(now);
    }

    /**
     * Extrait les informations d'un utilisateur du token.
     *
     * @param token le JWT
     * @return un objet UserResponse avec les informations extraites
     */
    public UserResponse getUser(String token) {
        Integer id = getId(token);
        String username = getUsername(token);
        String email = getEmail(token);
        List<String> roles = getRoles(token);

        return new UserResponse(id, username, email, roles);
    }

    /**
     * Extrait les claims (données) du token signé.
     *
     * @param token le JWT
     * @return les claims du token
     */
    private Claims getClaims(String token) {
        return parser.parseSignedClaims(token).getPayload();
    }

    /**
     * Obtient la clé secrète utilisée pour signer les tokens.
     *
     * @return la clé secrète
     *
     * @deprecated La clé secrète ne devrait pas être en dur.
     *             À remplacer par une variable d'environnement ou une configuration externe.
     */
    private SecretKey getSecretKey() {
        String secret = "Yabadabadoooooooooooooooooooooooooooooooooooooooooooooooooooooooooo";
        byte[] encoded = secret.getBytes();
        return new SecretKeySpec(encoded, "HmacSHA256");
    }
}
