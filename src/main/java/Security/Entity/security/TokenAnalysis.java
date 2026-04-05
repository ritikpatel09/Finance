package Security.Entity.security;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

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
    @JsonBackReference
    @ToString.Exclude
    private UserData userData;
}
