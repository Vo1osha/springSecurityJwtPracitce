package com.security.security.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String generateToken(Authentication authentication);

    Claims parseToken(String token);

    boolean validateToken(String token);
}
