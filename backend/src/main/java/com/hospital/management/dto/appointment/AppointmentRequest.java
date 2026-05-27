package com.hospital.management.dto.appointment;

import com.hospital.management.entity.AppointmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentRequest(
        @NotNull(message = "Patient id is required")
        Long patientId,

        @NotNull(message = "Doctor id is required")
        Long doctorId,

        @NotNull(message = "Appointment date/time is required")
        LocalDateTime appointmentDateTime,

        @NotBlank(message = "Reason is required")
        String reason,

        AppointmentStatus status,

        String notes
) {
}