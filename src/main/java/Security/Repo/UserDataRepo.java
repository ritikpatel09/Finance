package Security.Repo;

import Security.Entity.security.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserDataRepo  extends JpaRepository<UserData,Integer > {
    Optional<UserData> findByUsername(String username);
    @Query("SELECT u.id FROM UserData u WHERE u.username = :username")
    Optional<Integer> findIdByUsername(@Param("username") String username);
}
