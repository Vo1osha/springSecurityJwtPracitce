package com.security.security.service;

import com.security.security.config.JwtConfig;
import com.security.security.entity.Employee;
import com.security.security.entity.EntityPayload.AuthResponse;
import com.security.security.entity.Role;
import com.security.security.security.EmployeeUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
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

//        Set<String> roles = employee.getRoles().stream()
//                .map(Role::getName)
//                .collect(Collectors.toSet());


        return Jwts.builder()
                .subject(authentication.getName())
               //  .claim("roles", roles)
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
    public AuthResponse validateTokenDetailed(String authHeader){
        try {
                String token = validateAndExtractToken(authHeader);


            Claims claims = parseToken(token);
            String username = claims.getSubject();
            Long employeeId = claims.get("employeeId", Long.class);

            log.info("Token validated for employee: {}, id: {}", username, employeeId);
            Employee employee = this.employeeService.findEmployeeById(employeeId);

            AuthResponse authResponse = AuthResponse.builder()
                    .id(employee.getId())
                    .login(employee.getLogin())
                    .fullName(employee.getFullName())
                    .roles(employee.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toSet()))
                    .build();
            return authResponse;

        } catch (JwtException ex) {
            log.debug("Invalid JWT token: {}", ex.getMessage());
            throw new RuntimeException("Invalid JWT token" + ex.getMessage());
        }
    }
    public String validateAndExtractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        return authHeader.substring(7);
    }
    }

