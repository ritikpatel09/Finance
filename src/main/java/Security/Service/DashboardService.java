package Security.Service;


import Security.Dto.RequestDto.DashboardSummary;
import Security.Entity.security.FInance.FinancialRecord;
import Security.Repo.FinanceRepo;
import Security.Util.BaseApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FinanceRepo recordRepository;

    public BaseApiResponse getSummary(String username) {

        List<FinancialRecord> records =
                recordRepository.findByUserDataUsername(username);

        double income = records.stream()
                .filter(r -> "INCOME".equalsIgnoreCase(r.getType()))
                .mapToDouble(FinancialRecord::getAmount)
                .sum();

        double expense = records.stream()
                .filter(r -> "EXPENSE".equalsIgnoreCase(r.getType()))
                .mapToDouble(FinancialRecord::getAmount)
                .sum();

        DashboardSummary summary = DashboardSummary.builder()
                .totalIncome(income)
                .totalExpense(expense)
                .netBalance(income - expense)
                .build();

        return BaseApiResponse.ok("Dashboard Fetched",1,"200",summary);

    }
}