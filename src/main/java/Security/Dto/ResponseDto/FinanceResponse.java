package Security.Dto.ResponseDto;



import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FinanceResponse {

    private Integer id;

    private Double amount;

    private String type; // INCOME / EXPENSE

    private String category;

    private LocalDate date;

    private String description;
}
