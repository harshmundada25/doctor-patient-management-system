package com.hospital.management.controller;

import com.hospital.management.dto.patient.PatientRequest;
import com.hospital.management.dto.patient.PatientResponse;
import com.hospital.management.security.UserPrincipal;
import com.hospital.management.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PatientResponse> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public PatientResponse getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PATIENT')")
    public PatientResponse myProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return patientService.getMyProfile(principal);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PatientResponse createPatient(@Valid @RequestBody PatientRequest request) {
        return patientService.createPatient(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PatientResponse updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRequest request) {
        return patientService.updatePatient(id, request);
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('PATIENT')")
    public PatientResponse updateMyProfile(@AuthenticationPrincipal UserPrincipal principal, @Valid @RequestBody PatientRequest request) {
        return patientService.updateMyProfile(principal, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}