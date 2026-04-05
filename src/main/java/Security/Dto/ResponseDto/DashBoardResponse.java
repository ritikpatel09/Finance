package Security.Dto.ResponseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashBoardResponse {

    private Double totalIncome;
    private Double totalExpense;
    private Double netBalance;
}