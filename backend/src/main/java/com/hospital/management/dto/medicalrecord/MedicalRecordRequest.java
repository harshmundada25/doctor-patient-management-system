package com.hospital.management.dto.medicalrecord;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MedicalRecordRequest(
        @NotNull(message = "Patient id is required")
        Long patientId,

        @NotNull(message = "Doctor id is required")
        Long doctorId,

        @NotBlank(message = "Diagnosis is required")
        String diagnosis,

        String treatment,

        String allergies,

        String notes,

        LocalDateTime visitDate
) {
}