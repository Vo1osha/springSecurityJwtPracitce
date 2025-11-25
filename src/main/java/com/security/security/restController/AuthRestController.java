package com.security.security.restController;

import com.security.security.entity.Employee;
import com.security.security.entity.EntityPayload.AuthRequest;
import com.security.security.entity.EntityPayload.AuthResponse;
import com.security.security.security.EmployeeUserDetails;
import com.security.security.service.DefaultEmployeeService;
import com.security.security.service.DefaultJwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("v1/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthRestController {

    private final DefaultJwtService defaultJwtService;
    private final DefaultEmployeeService defaultEmployeeService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
        Employee employee = this.defaultEmployeeService.findEmployeeByLogin(authRequest.username());
        this.defaultEmployeeService.validatePAssword(employee, authRequest.password());

        String token = defaultJwtService.generateToken(employee.getLogin(),new EmployeeUserDetails(employee));


        AuthResponse authResponse = new AuthResponse(
                employee.getId(),
                employee.getLogin(),
                employee.getFullName(),
                employee.getRoles()
        );

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false) // true в prod
                .path("/")
                .maxAge(86400)
                .sameSite("Lax")
                .build();

        log.info("User auth successfully {} ID: {} with roles {}",
                employee.getLogin(),
                employee.getId(),
                employee.getRoles());

        return   ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of(
                        "token", token,
                        "type", "Bearer",
                        "user", authResponse
                        ));
    }
}
