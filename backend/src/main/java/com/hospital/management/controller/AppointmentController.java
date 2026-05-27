package com.hospital.management.controller;

import com.hospital.management.dto.appointment.AppointmentRequest;
import com.hospital.management.dto.appointment.AppointmentResponse;
import com.hospital.management.entity.AppointmentStatus;
import com.hospital.management.entity.Patient;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.security.UserPrincipal;
import com.hospital.management.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientRepository patientRepository;

    public AppointmentController(AppointmentService appointmentService, PatientRepository patientRepository) {
        this.appointmentService = appointmentService;
        this.patientRepository = patientRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public List<AppointmentResponse> getMyAppointments(@AuthenticationPrincipal UserPrincipal principal) {
        return appointmentService.getMyAppointments(principal);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    public AppointmentResponse bookAppointment(@AuthenticationPrincipal UserPrincipal principal, @Valid @RequestBody AppointmentRequest request) {
        if (principal.getRole().name().equals("PATIENT")) {
            Patient patient = patientRepository.findByUser_Id(principal.getUserId()).orElseThrow(() -> new com.hospital.management.exception.ResourceNotFoundException("Patient profile not found"));
            AppointmentRequest securedRequest = new AppointmentRequest(patient.getId(), request.doctorId(), request.appointmentDateTime(), request.reason(), request.status(), request.notes());
            return appointmentService.bookAppointment(securedRequest);
        }
        return appointmentService.bookAppointment(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public AppointmentResponse updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentRequest request) {
        return appointmentService.updateAppointment(id, request);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public AppointmentResponse updateStatus(@PathVariable Long id, @RequestParam AppointmentStatus status) {
        return appointmentService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
    }
}