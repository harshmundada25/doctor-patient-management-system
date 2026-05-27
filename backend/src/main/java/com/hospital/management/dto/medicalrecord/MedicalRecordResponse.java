package com.hospital.management.dto.medicalrecord;

import java.time.LocalDateTime;

public record MedicalRecordResponse(
        Long id,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        String diagnosis,
        String treatment,
        String allergies,
        String notes,
        LocalDateTime visitDate
) {
}