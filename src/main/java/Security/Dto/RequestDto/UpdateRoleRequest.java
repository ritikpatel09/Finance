package Security.Dto.RequestDto;

import lombok.Data;

@Data
public class UpdateRoleRequest {

    private Integer userId;
    private String role; // ADMIN / ANALYST / VIEWER
}