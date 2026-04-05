package Security.Repo;

import Security.Entity.security.FInance.FinancialRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinanceRepo extends JpaRepository<FinancialRecord,Integer> {

    List<FinancialRecord> findByUserDataUsername(String Username);

    Page<FinancialRecord> findByUserDataId(Integer userId, Pageable pageable);
    Page<FinancialRecord> findAll(Pageable pageable);
}
