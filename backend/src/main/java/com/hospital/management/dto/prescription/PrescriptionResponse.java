package com.hospital.management.dto.prescription;

import java.time.LocalDate;

public record PrescriptionResponse(
        Long id,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        String medicationName,
        String dosage,
        String frequency,
        String duration,
        String notes,
        LocalDate prescribedDate
) {
}