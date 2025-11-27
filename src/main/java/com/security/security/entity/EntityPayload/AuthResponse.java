package com.security.security.entity.EntityPayload;

import com.security.security.entity.Role;
import lombok.Builder;

import java.util.Set;

@Builder
public record AuthResponse(
        Long id,
        String login,
        String fullName,
        Set<String> roles
) { }
