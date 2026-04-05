package Security.Service;

import Security.Dto.RequestDto.FinanceRequestDto;
import Security.Dto.RequestDto.UpdateRoleRequest;
import Security.Dto.ResponseDto.FinanceResponse;
import Security.Entity.security.FInance.FinancialRecord;
import Security.Entity.security.TokenAnalysis;
import Security.Entity.security.UserData;
import Security.Exception.CustomException;
import Security.Exception.NotFoundException;
import Security.Repo.FinanceRepo;
import Security.Repo.TokenAnalysisRepo;
import Security.Repo.UserDataRepo;
import Security.Util.BaseApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinancialService {

    private final FinanceRepo recordRepository;
    private final TokenAnalysisRepo tokenRepo;
    private final UserDataRepo userDataRepo;

    private UserData getUserFromToken(String loginToken) {
        TokenAnalysis session = tokenRepo.findByLoginToken(loginToken)
                .orElseThrow(() -> new RuntimeException("Invalid session"));
        UserData user = session.getUserData();

        if (user.isLockAccount() || user.getIsActive() == 0) {
            throw new AccessDeniedException("Account locked or inactive");
        }
        return user;
    }

    public BaseApiResponse createRecord(FinanceRequestDto request, String loginToken) {
        UserData loggedInUser = getUserFromToken(loginToken);
        if(!loggedInUser.getRole().equals("ADMIN")){
            throw new CustomException("NOT ALLOWEED TO CREATE RECORD");
        }
        UserData targetUser = userDataRepo.findById(request.getUserId())
                .orElseThrow(() -> new CustomException("User not found"));

        FinancialRecord record = FinancialRecord.builder()
                .userData(targetUser)
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .date(LocalDate.now())
                .description(request.getDescription())
                .build();

        recordRepository.save(record);

        return BaseApiResponse.ok("Record created", 1, "200");
    }


    public BaseApiResponse getAllRecordsByUser(String loginToken, int page) {

        UserData user = getUserFromToken(loginToken);

        checkRole(user.getRole(), "ADMIN", "ANALYST");

        Pageable pageable = PageRequest.of(page, 10);

        Page<FinancialRecord> recordPage =
                recordRepository.findByUserDataId(user.getId(), pageable);

        List<FinanceResponse> records = recordPage
                .stream()
                .map(this::mapToResponse)
                .toList();

        return BaseApiResponse.ok(
                "Records fetched",
                1,
                "200",
                records
        );
    }

    // 🔥 UPDATE
    public BaseApiResponse updateRecord( Integer id, FinanceRequestDto request, String loginToken) {

        UserData user = getUserFromToken(loginToken);

        checkRole(user.getRole(), "ADMIN");

        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        if (!record.getUserData().getId().equals(user.getId())) {
            throw new AccessDeniedException("Not your record");
        }

        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDate(LocalDate.now());
        record.setDescription(request.getDescription());

        recordRepository.save(record);

        return BaseApiResponse.ok("Updated",1, "200");
    }

    public BaseApiResponse deleteRecord(Integer id, String loginToken) {

        UserData user = getUserFromToken(loginToken);

        checkRole(user.getRole(), "ADMIN");

        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        if (!record.getUserData().getId().equals(user.getId())) {
            throw new AccessDeniedException("Not your record");
        }

        recordRepository.delete(record);

        return BaseApiResponse.ok("Deleted", 1, "200",null);
    }

    private FinanceResponse mapToResponse(FinancialRecord record) {
        return FinanceResponse.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .type(record.getType())
                .category(record.getCategory())
                .date(record.getDate())
                .description(record.getDescription())
                .build();
    }

    private void checkRole(String userRole, String... allowedRoles) {
        for (String role : allowedRoles) {
            if (role.equalsIgnoreCase(userRole)) return;
        }
        throw new AccessDeniedException("Access denied");
    }


    public BaseApiResponse updateUserRole(UpdateRoleRequest request, String loginToken) {

        UserData adminUser = getUserFromToken(loginToken);

        if (!"ADMIN".equalsIgnoreCase(adminUser.getRole())) {
            throw new AccessDeniedException("Only ADMIN can change roles");
        }

        List<String> allowedRoles = List.of("ADMIN", "ANALYST", "VIEWER");

        if (!allowedRoles.contains(request.getRole().toUpperCase())) {
            throw new CustomException("Invalid role");
        }

        UserData targetUser = userDataRepo.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        targetUser.setRole(request.getRole().toUpperCase());

        userDataRepo.save(targetUser);

        return BaseApiResponse.ok("User role updated", targetUser.getId(), "200");
    }


    public BaseApiResponse getAllRecordsAdmin(String loginToken, int page) {
        System.out.println("test 1");
        UserData user = getUserFromToken(loginToken);

        checkRole(user.getRole(), "ADMIN", "ANALYST");

        Pageable pageable = PageRequest.of(page, 10);

        Page<FinancialRecord> recordPage = recordRepository.findAll(pageable);

        List<FinanceResponse> records = recordPage
                .stream()
                .map(this::mapToResponse)
                .toList();

        return BaseApiResponse.ok(
                "All records fetched",
                1,
                "200",
               records
        );
    }
}