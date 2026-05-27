package com.hospital.management.controller;

import com.hospital.management.dto.prescription.PrescriptionRequest;
import com.hospital.management.dto.prescription.PrescriptionResponse;
import com.hospital.management.entity.Doctor;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.security.UserPrincipal;
import com.hospital.management.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final DoctorRepository doctorRepository;

    public PrescriptionController(PrescriptionService prescriptionService, DoctorRepository doctorRepository) {
        this.prescriptionService = prescriptionService;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PrescriptionResponse> getAllPrescriptions() {
        return prescriptionService.getAllPrescriptions();
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public List<PrescriptionResponse> getMyPrescriptions(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal.getRole().name().equals("DOCTOR")) {
            return prescriptionService.getPrescriptionsForDoctor(principal.getUserId());
        }
        return prescriptionService.getPrescriptionsForPatient(principal.getUserId());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public PrescriptionResponse createPrescription(@AuthenticationPrincipal UserPrincipal principal, @Valid @RequestBody PrescriptionRequest request) {
        if (principal.getRole().name().equals("DOCTOR")) {
            Doctor doctor = doctorRepository.findByUser_Id(principal.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));
            PrescriptionRequest securedRequest = new PrescriptionRequest(request.patientId(), doctor.getId(), request.medicationName(), request.dosage(), request.frequency(), request.duration(), request.notes(), request.prescribedDate());
            return prescriptionService.createPrescription(securedRequest);
        }
        return prescriptionService.createPrescription(request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
    }
}