package com.hospital.management.dto.dashboard;

import com.hospital.management.entity.AppointmentStatus;

import java.time.LocalDateTime;

public record RecentAppointmentResponse(
        Long id,
        String patientName,
        String doctorName,
        LocalDateTime appointmentDateTime,
        AppointmentStatus status
) {
}