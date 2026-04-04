package Security.Service;

import Security.Dto.RegisterDto;
import Security.Entity.security.TokenAnalysis;
import Security.Entity.security.UserData;
import Security.Exception.InvalidException;
import Security.Exception.NotFoundException;
import Security.Filter.Jwtservice;
import Security.Filter.TokenGenerator;
import Security.Repo.TokenAnalysisRepo;
import Security.Repo.UserDataRepo;
import Security.Util.BaseApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Jwtservice jwtservice;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDataRepo userDataRepo;
    private final TokenAnalysisRepo tokenRepo;
    private final TokenGenerator tokenGenerator;


    public BaseApiResponse register(RegisterDto dto) {

        UserData userData = UserData.builder()
                .username(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .isActive(1)
                .lockAccount(false)
                .role("USER")
                .build();

        userDataRepo.save(userData);

        return BaseApiResponse.ok("User registered", 1, "200");
    }


    public BaseApiResponse authenticate(RegisterDto dto, HttpServletRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getEmail(),
                            dto.getPassword()
                    )
            );

            UserData userData = userDataRepo.findByUsername(dto.getEmail())
                    .orElseThrow(() -> new NotFoundException("User not found"));

            if (userData.isLockAccount()) {
                return BaseApiResponse.ok("Account Locked", 0, "403");
            }

            if (userData.getIsActive() == 0) {
                return BaseApiResponse.ok("Account Inactive", 0, "403");
            }

            String loginToken = tokenGenerator.generateLoginToken();

            TokenAnalysis analysis = new TokenAnalysis();
            analysis.setLoginToken(loginToken);
            analysis.setUserData(userData);
            analysis.setCreatedAt(LocalDateTime.now());
            analysis.setIp(getClientIp(request));
            analysis.setBrowser(request.getHeader("User-Agent"));
            tokenRepo.save(analysis);

            String token = jwtservice.generateToken(loginToken);
            return BaseApiResponse.authResponse("Authorized", 1, "200", token);

        } catch (InvalidException e) {
            throw new InvalidException("Email or Password is Wrong");
        }
    }
    public String getClientIp(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }

        return ip;
    }
}