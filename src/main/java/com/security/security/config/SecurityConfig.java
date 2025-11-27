package com.security.security.config;

import com.security.security.filter.JwtAuthFilter;
import com.security.security.security.jwt.AuthEntryPointJwt;
import com.security.security.security.jwt.JwtTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtTokenValidator jwtTokenValidator;
    private final AuthEntryPointJwt authEntryPointJwt;

    @Bean
   SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
       http.csrf(csrf -> csrf.disable())
               .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authorizeHttpRequests(auth ->
                       auth.requestMatchers("/api/v1/auth/validate").hasRole("USER")
                               .anyRequest().authenticated())
               .exceptionHandling(exceptions -> exceptions
                       .authenticationEntryPoint(authEntryPointJwt))

               .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

       return http.build();
   }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtTokenValidator);
    }


    @Bean
   PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
   }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
