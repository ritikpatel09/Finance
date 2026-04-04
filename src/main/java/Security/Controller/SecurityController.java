package Security.Controller;
import Security.Dto.RegisterDto;
import Security.Service.UserService;
import Security.Util.BaseApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<BaseApiResponse> register(@RequestBody RegisterDto dto) {
        BaseApiResponse response = userService.register(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseApiResponse> login(
            @Valid @RequestBody RegisterDto dto,
            HttpServletRequest request) {

        BaseApiResponse response = userService.authenticate(dto, request);
        return ResponseEntity.ok(response);
    }
}