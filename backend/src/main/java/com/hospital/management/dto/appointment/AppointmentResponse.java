package com.hospital.management.dto.appointment;

import com.hospital.management.entity.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        LocalDateTime appointmentDateTime,
        String reason,
        AppointmentStatus status,
        String notes
) {
}