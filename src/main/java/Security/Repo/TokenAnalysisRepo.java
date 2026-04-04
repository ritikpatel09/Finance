package Security.Repo;

import Security.Entity.security.TokenAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenAnalysisRepo extends JpaRepository<TokenAnalysis,Integer> {
    Optional<TokenAnalysis> findByLoginToken(String loginToken);
}
