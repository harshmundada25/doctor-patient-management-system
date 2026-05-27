package com.hospital.management.dto.doctor;

public record DoctorResponse(
        Long id,
        Long userId,
        String fullName,
        String email,
        String phone,
        String specialty,
        String licenseNumber,
        Integer yearsOfExperience,
        String availability,
        Boolean active
) {
}