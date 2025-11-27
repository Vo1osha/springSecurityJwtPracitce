package com.security.security.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        log.warn("Unauthorized access to {}: {}", request.getServletPath(), authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Твоя структура ответа
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", getErrorMessage(authException));
        body.put("path", request.getServletPath());
        body.put("timestamp", Instant.now().toString());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }

    private String getErrorMessage(AuthenticationException authException) {
        // Более понятные сообщения об ошибках
        if (authException instanceof BadCredentialsException) {
            return "Invalid login or password";
        } else if (authException instanceof InsufficientAuthenticationException) {
            return "Full authentication is required to access this resource";
        } else if (authException.getCause() instanceof ExpiredJwtException) {
            return "JWT token has expired";
        } else if (authException.getCause() instanceof JwtException) {
            return "Invalid JWT token";
        }
        return authException.getMessage();
    }
}

