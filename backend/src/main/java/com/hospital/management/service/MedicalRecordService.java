package com.hospital.management.service;

import com.hospital.management.dto.medicalrecord.MedicalRecordRequest;
import com.hospital.management.dto.medicalrecord.MedicalRecordResponse;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.MedicalRecord;
import com.hospital.management.entity.Patient;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.MedicalRecordRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.util.DomainMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DomainMapper mapper;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, DomainMapper mapper) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> getRecordsForPatient(Long patientId) {
        return medicalRecordRepository.findByPatient_Id(patientId).stream().map(mapper::toMedicalRecordResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> getRecordsForDoctor(Long doctorId) {
        return medicalRecordRepository.findByDoctor_Id(doctorId).stream().map(mapper::toMedicalRecordResponse).toList();
    }

    @Transactional
    public MedicalRecordResponse createOrUpdate(MedicalRecordRequest request) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.patientId()));
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.doctorId()));

        MedicalRecord record = MedicalRecord.builder()
                .patient(patient)
                .doctor(doctor)
                .diagnosis(request.diagnosis())
                .treatment(request.treatment())
                .allergies(request.allergies())
                .notes(request.notes())
                .visitDate(request.visitDate() == null ? LocalDateTime.now() : request.visitDate())
                .build();
        return mapper.toMedicalRecordResponse(medicalRecordRepository.save(record));
    }

    @Transactional
    public void deleteRecord(Long id) {
        medicalRecordRepository.deleteById(id);
    }
}