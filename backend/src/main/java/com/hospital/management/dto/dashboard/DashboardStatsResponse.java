package com.hospital.management.dto.dashboard;

import java.util.List;

public record DashboardStatsResponse(
        String role,
        long doctorCount,
        long patientCount,
        long appointmentCount,
        long prescriptionCount,
        long medicalRecordCount,
        long pendingAppointments,
        long completedAppointments,
        long cancelledAppointments,
        List<RecentAppointmentResponse> recentAppointments
) {
}