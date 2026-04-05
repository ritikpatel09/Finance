package Security.Controller;

import Security.Dto.RequestDto.FinanceRequestDto;
import Security.Dto.RequestDto.UpdateRoleRequest;
import Security.Service.FinancialService;
import Security.Util.BaseApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class FinanceController {

    private final FinancialService financialService;

    @PostMapping("/createRecord")
    public ResponseEntity<BaseApiResponse> createRecord(
            @RequestBody FinanceRequestDto request,
            Authentication authentication
    ) {

        String loginToken = (String) authentication.getPrincipal();

        return ResponseEntity.ok(
                financialService.createRecord(request, loginToken)
        );
    }

    @GetMapping("/getRecord/{page}")
    public ResponseEntity<BaseApiResponse> getRecords(
            Authentication authentication,@PathVariable int page) {

        String loginToken = (String) authentication.getPrincipal();

        return ResponseEntity.ok(
                financialService.getAllRecordsByUser(loginToken,page)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseApiResponse> updateRecord(
            @PathVariable Integer id,
            @RequestBody FinanceRequestDto request,
            Authentication authentication
    ) {

        String loginToken = (String) authentication.getPrincipal();

        return ResponseEntity.ok(
                financialService.updateRecord(id, request, loginToken)
        );
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<BaseApiResponse> deleteRecord(
            @PathVariable Integer id,
            Authentication authentication
    ) {

        String loginToken = (String) authentication.getPrincipal();

        return ResponseEntity.ok(
                financialService.deleteRecord(id, loginToken)
        );
    }

    @PutMapping("/Updaterole")
    public ResponseEntity<BaseApiResponse> updateUserRole(
            @RequestBody UpdateRoleRequest request,
            Authentication authentication
    ) {

        String loginToken = (String) authentication.getPrincipal();

        return ResponseEntity.ok(
                financialService.updateUserRole(request, loginToken)
        );
    }

    @PostMapping("/all/{page}")
    public ResponseEntity<BaseApiResponse> getAllRecordsAdmin(
            @PathVariable int page,
            Authentication authentication
    ) {

        String loginToken = (String) authentication.getPrincipal();

        return ResponseEntity.ok(
                financialService.getAllRecordsAdmin(loginToken, page)
        );
    }
}