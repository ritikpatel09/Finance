package Security.Entity.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserData implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String username;
    private String password;
    private String role;
    private int isActive;
    private boolean lockAccount;

    @OneToMany(mappedBy = "userData",fetch = FetchType.LAZY)
    private List<TokenAnalysis> tokenAnalysis;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(()->role);
    }
}
