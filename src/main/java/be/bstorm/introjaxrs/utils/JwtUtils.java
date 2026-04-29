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

@ApplicationScoped
public class JwtUtils {

    private final JwtBuilder builder;
    private final JwtParser parser;

    public JwtUtils() {
        this.builder = Jwts.builder().signWith(getSecretKey());
        this.parser = Jwts.parser().verifyWith(getSecretKey()).build();
    }

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

    public int getId(String token) {
        return Integer.parseInt(getClaims(token).getId());
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String getEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        return (List<String>) getClaims(token).get("roles", List.class);
    }

    public boolean isValid(String token){
        Date now = new Date();
        return getClaims(token).getIssuedAt().before(now) && getClaims(token).getExpiration().after(now);
    }

    public UserResponse getUser(String token) {
        Integer id = getId(token);
        String username = getUsername(token);
        String email = getEmail(token);
        List<String> roles = getRoles(token);

        return new UserResponse(id, username, email, roles);
    }

    private Claims getClaims(String token) {
        return parser.parseSignedClaims(token).getPayload();
    }

    private SecretKey getSecretKey() {
        String secret = "Yabadabadoooooooooooooooooooooooooooooooooooooooooooooooooooooooooo";
        byte[] encoded = secret.getBytes();
        return new SecretKeySpec(encoded, "HmacSHA256");
    }
}
