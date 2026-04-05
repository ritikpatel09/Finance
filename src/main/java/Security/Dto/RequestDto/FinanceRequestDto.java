package Security.Dto.RequestDto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class FinanceRequestDto {
    private Integer userId;
    private Double amount;
    private String type;
    private String category;
    private String description;
}