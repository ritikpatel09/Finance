package Security.Dto.RequestDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
@Data
public class RegisterDto {
    @NotBlank(message = "Email not be Empty")
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Pattern(
            regexp = "^.{8,}$",
            message = "Password must be at least 8 characters long"
    )
    private String password;

}
