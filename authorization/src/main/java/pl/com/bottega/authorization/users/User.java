package pl.com.bottega.authorization.users;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
@NamedEntityGraph(name = "User.withRoles",
        attributeNodes = @NamedAttributeNode("roles")
)
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long employeeId;

    private String username;

    private String password;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "user")
    private Set<UserRole> roles = new HashSet<>();
}
