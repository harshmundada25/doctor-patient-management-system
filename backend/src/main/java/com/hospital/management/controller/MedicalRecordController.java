package com.hospital.management.controller;

import com.hospital.management.dto.medicalrecord.MedicalRecordRequest;
import com.hospital.management.dto.medicalrecord.MedicalRecordResponse;
import com.hospital.management.security.UserPrincipal;
import com.hospital.management.service.MedicalRecordService;
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
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public List<MedicalRecordResponse> getMyRecords(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal.getRole().name().equals("DOCTOR")) {
            return medicalRecordService.getRecordsForDoctor(principal.getUserId());
        }
        return medicalRecordService.getRecordsForPatient(principal.getUserId());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public MedicalRecordResponse create(@Valid @RequestBody MedicalRecordRequest request) {
        return medicalRecordService.createOrUpdate(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public MedicalRecordResponse update(@PathVariable Long id, @Valid @RequestBody MedicalRecordRequest request) {
        return medicalRecordService.createOrUpdate(request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        medicalRecordService.deleteRecord(id);
    }
}