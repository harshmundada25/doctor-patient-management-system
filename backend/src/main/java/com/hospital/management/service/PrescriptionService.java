package com.hospital.management.service;

import com.hospital.management.dto.prescription.PrescriptionRequest;
import com.hospital.management.dto.prescription.PrescriptionResponse;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.Prescription;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.PrescriptionRepository;
import com.hospital.management.util.DomainMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DomainMapper mapper;

    public PrescriptionService(PrescriptionRepository prescriptionRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, DomainMapper mapper) {
        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream().map(mapper::toPrescriptionResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getPrescriptionsForPatient(Long patientId) {
        return prescriptionRepository.findByPatient_Id(patientId).stream().map(mapper::toPrescriptionResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getPrescriptionsForDoctor(Long doctorId) {
        return prescriptionRepository.findByDoctor_Id(doctorId).stream().map(mapper::toPrescriptionResponse).toList();
    }

    @Transactional
    public PrescriptionResponse createPrescription(PrescriptionRequest request) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.patientId()));
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.doctorId()));

        Prescription prescription = Prescription.builder()
                .patient(patient)
                .doctor(doctor)
                .medicationName(request.medicationName())
                .dosage(request.dosage())
                .frequency(request.frequency())
                .duration(request.duration())
                .notes(request.notes())
                .prescribedDate(request.prescribedDate() == null ? LocalDate.now() : request.prescribedDate())
                .build();
        return mapper.toPrescriptionResponse(prescriptionRepository.save(prescription));
    }

    @Transactional
    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }
}