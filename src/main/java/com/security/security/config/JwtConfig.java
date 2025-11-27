package com.security.security.config;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
@Data
public class JwtConfig {


    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration:3600000}")
    private long expiration;

    @Bean
    public SecretKey jwtKey(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    @Bean
    public Long jwtExpiration(){
        return expiration;
    }

}