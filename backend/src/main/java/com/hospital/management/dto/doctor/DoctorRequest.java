package com.hospital.management.dto.doctor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record DoctorRequest(
        @NotBlank(message = "Doctor name is required")
        String fullName,

        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Phone is required")
        String phone,

        @NotBlank(message = "Specialty is required")
        String specialty,

        @NotBlank(message = "License number is required")
        String licenseNumber,

        String password,

        @NotNull(message = "Years of experience is required")
        @PositiveOrZero(message = "Experience cannot be negative")
        Integer yearsOfExperience,

        String availability,

        Boolean active
) {
}