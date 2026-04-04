package Security.Entity.security;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class TokenAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer autoId;
    private String loginToken;
    private LocalDateTime createdAt;
    private String ip;
    private String browser;

    @ManyToOne
    @JoinColumn(name = "userId" )
    private UserData userData;
}
