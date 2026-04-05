package Security.Controller;
import Security.Service.DashboardService;
import Security.Util.BaseApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<BaseApiResponse> getSummary(Authentication auth) {

        BaseApiResponse response =
                dashboardService.getSummary(auth.getName());

        return ResponseEntity.ok(response);
    }
}
