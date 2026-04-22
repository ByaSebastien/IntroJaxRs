package be.bstorm.introjaxrs.pojos;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_")
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode @ToString
public class User {

    @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter @Setter
    @Column(length = 150, nullable = false, unique = true)
    private String email;

    @Getter @Setter
    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Getter @Setter
    @Column(nullable = false)
    private String password;

    @Getter
    @ManyToMany
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role){
        this.roles.add(role);
    }

    public void removeRole(Role role){
        this.roles.remove(role);
    }
}
