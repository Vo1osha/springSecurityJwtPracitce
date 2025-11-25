package com.security.security.entity.EntityPayload;

public record AuthRequest(
        String username,
        String password
) {

}
