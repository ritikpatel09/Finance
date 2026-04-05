package Security.Filter;

import Security.Entity.security.TokenAnalysis;
import Security.Entity.security.UserData;
import Security.Repo.TokenAnalysisRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final Jwtservice jwtservice;
    private final TokenAnalysisRepo tokenRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");

        String token = null;
        String loginToken = null;

        // 🔹 Extract JWT
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            loginToken = jwtservice.extractLoginToken(token);
        }

        if (loginToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 🔥 Find session ONLY by loginToken
            TokenAnalysis session = tokenRepo
                    .findByLoginToken(loginToken)
                    .orElse(null);

            if (session != null && jwtservice.isTokenValid(token)) {

                UserData user = session.getUserData();

                // 🔐 Check account status
                if (user.isLockAccount() || user.getIsActive() ==0) {
                    throw new RuntimeException("Account locked or inactive");
                }

                UserDetails userDetails = new User(
                        user.getUsername(),
                        user.getPassword(),
                        List.of(() -> user.getRole())
                );

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                loginToken,   // 🔥 THIS IS KEY
                                null,
                                user.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }
}