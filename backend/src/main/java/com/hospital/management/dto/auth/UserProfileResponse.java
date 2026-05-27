package com.hospital.management.dto.auth;

public record UserProfileResponse(
        Long userId,
        String fullName,
        String email,
        String role,
        boolean active
) {
}