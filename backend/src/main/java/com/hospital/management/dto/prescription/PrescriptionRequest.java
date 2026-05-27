package com.hospital.management.dto.prescription;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PrescriptionRequest(
        @NotNull(message = "Patient id is required")
        Long patientId,

        @NotNull(message = "Doctor id is required")
        Long doctorId,

        @NotBlank(message = "Medication name is required")
        String medicationName,

        @NotBlank(message = "Dosage is required")
        String dosage,

        @NotBlank(message = "Frequency is required")
        String frequency,

        String duration,

        String notes,

        LocalDate prescribedDate
) {
}