package com.expoai.bucket.config;

import com.expoai.bucket.entity.User;
import com.expoai.bucket.repository.ApiTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final ApiTokenRepository tokenRepo;

    public ApiKeyAuthFilter(ApiTokenRepository tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            tokenRepo.findByTokenAndRevokedFalse(token)
                    .ifPresent(apiToken -> {
                        User user = apiToken.getUser();
                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                user.getUsername(), null, List.of(new SimpleGrantedAuthority("ROLE_API"))
                        );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    });
        }

        filterChain.doFilter(request, response);
    }
}
