package com.hospital.management.controller;

import com.hospital.management.dto.dashboard.DashboardStatsResponse;
import com.hospital.management.security.UserPrincipal;
import com.hospital.management.service.DashboardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public DashboardStatsResponse stats(@AuthenticationPrincipal UserPrincipal principal) {
        return switch (principal.getRole()) {
            case ADMIN -> dashboardService.getStats(principal);
            case DOCTOR -> dashboardService.getDoctorStats(principal);
            case PATIENT -> dashboardService.getPatientStats(principal);
        };
    }
}