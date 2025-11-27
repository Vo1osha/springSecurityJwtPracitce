package com.security.security.security.jwt;

import com.security.security.entity.Employee;
import com.security.security.entity.EntityPayload.AuthResponse;
import com.security.security.entity.Role;
import com.security.security.service.EmployeeService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class TokenValidationService {

    private final JwtService jwtService;
    private final EmployeeService employeeService;

    public AuthResponse validateTokenAndGetUserInfo(String authHeader) {
        String token = jwtService.extractTokenFromHeader(authHeader);
        Claims claims = jwtService.parseToken(token);

        Long employeeId = claims.get("employeeId", Long.class);
        Employee employee = employeeService.findEmployeeById(employeeId);

        return mapToAuthResponse(employee);
    }

    private AuthResponse mapToAuthResponse(Employee employee) {
        return AuthResponse.builder()
                .id(employee.getId())
                .login(employee.getLogin())
                .fullName(employee.getFullName())
                .roles(employee.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
