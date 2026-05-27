package com.hospital.management.dto.patient;

import com.hospital.management.entity.Gender;

import java.time.LocalDate;

public record PatientResponse(
        Long id,
        Long userId,
        String fullName,
        String email,
        String phone,
        LocalDate dateOfBirth,
        Gender gender,
        String bloodGroup,
        String address,
        String emergencyContact,
        Long assignedDoctorId,
        String assignedDoctorName
) {
}