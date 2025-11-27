package com.security.security.security.jwt;

import com.security.security.entity.Employee;
import com.security.security.entity.EntityPayload.AuthResponse;
import com.security.security.entity.Role;
import com.security.security.security.services.EmployeeUserDetails;
import com.security.security.service.EmployeeService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultJwtService implements JwtService{

    private final SecretKey jwtSecret;

    private final long jwtExp;

    private final EmployeeService employeeService;



    @Override
    public String generateToken(Authentication authentication) {

        EmployeeUserDetails employee = (EmployeeUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .subject(authentication.getName())
                .claim("employeeId",employee.getEmployeeId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExp ))
                .id(UUID.randomUUID().toString())
                .signWith( jwtSecret, Jwts.SIG.HS512)
                .compact();
    }

    @Override
    public Claims parseToken(String token) {
        return Jwts.parser()

                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public boolean validateToken(String token) {
        try{
            parseToken(token);
            return true;
        } catch (JwtException ex) {
            log.debug("Invalid JWT token {}",ex.getMessage());
            return false;
        }
    }

    public String extractTokenFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        return authHeader.substring(7);
    }
    }

