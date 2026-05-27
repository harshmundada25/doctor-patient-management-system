package com.hospital.management.dto.patient;

import com.hospital.management.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PatientRequest(
        @NotBlank(message = "Full name is required")
        String fullName,

        @NotBlank(message = "Email is required")
        String email,

        @NotBlank(message = "Phone is required")
        String phone,

        LocalDate dateOfBirth,

        @NotNull(message = "Gender is required")
        Gender gender,

        @NotBlank(message = "Blood group is required")
        String bloodGroup,

        @NotBlank(message = "Address is required")
        String address,

        String emergencyContact,

        String password,

        Long assignedDoctorId
) {
}