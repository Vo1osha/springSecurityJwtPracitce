package com.security.security.entity.EntityPayload;

import com.security.security.entity.Role;

import java.util.Set;

public record AuthResponse(
        Long id,
        String login,
        String fullName,
        Set<Role> roles
) {
}
