package com.security.security.restController;

import com.security.security.entity.EntityPayload.AuthRequest;
import com.security.security.entity.EntityPayload.AuthResponse;
import com.security.security.security.jwt.TokenValidationService;
import com.security.security.security.services.EmployeeUserDetails;
import com.security.security.service.DefaultEmployeeService;
import com.security.security.security.jwt.DefaultJwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthRestController {

    private final DefaultJwtService defaultJwtService;
    private final DefaultEmployeeService defaultEmployeeService;
    private final TokenValidationService tokenValidationService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = defaultJwtService.generateToken(authentication);


        EmployeeUserDetails userDetails = (EmployeeUserDetails) authentication.getPrincipal();




        AuthResponse authResponse = new AuthResponse(
                userDetails.getEmployeeId(),
                userDetails.getLogin(),
                userDetails.getFullName(),
                userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toSet()
        ));

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false) // true в prod
                .path("/")
                .maxAge(86400)
                .sameSite("Lax")
                .build();

        log.info("User auth successfully {} ID: {} - {} with roles {}",
                userDetails.getLogin(),
                userDetails.getEmployeeId(),
                userDetails.getFullName(),
                userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toSet()));

        return   ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of(
                        "token", token,
                        "type", "Bearer",
                        "user", authResponse
                        ));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader){
        AuthResponse authResponse = this.tokenValidationService.validateTokenAndGetUserInfo(authHeader);
        return ResponseEntity.ok().body(authResponse);
    }
}
