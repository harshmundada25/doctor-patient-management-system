package com.hospital.management.service;

import com.hospital.management.dto.patient.PatientRequest;
import com.hospital.management.dto.patient.PatientResponse;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.Role;
import com.hospital.management.entity.User;
import com.hospital.management.exception.DuplicateResourceException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.security.UserPrincipal;
import com.hospital.management.util.DomainMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final DomainMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepository patientRepository, UserRepository userRepository, DoctorRepository doctorRepository, DomainMapper mapper, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream().map(mapper::toPatientResponse).toList();
    }

    @Transactional(readOnly = true)
    public PatientResponse getPatientById(Long id) {
        return mapper.toPatientResponse(findPatient(id));
    }

    @Transactional(readOnly = true)
    public PatientResponse getMyProfile(UserPrincipal principal) {
        Patient patient = patientRepository.findByUser_Id(principal.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
        return mapper.toPatientResponse(patient);
    }

    @Transactional
    public PatientResponse updateMyProfile(UserPrincipal principal, PatientRequest request) {
        Patient patient = patientRepository.findByUser_Id(principal.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
        return updatePatient(patient.getId(), request);
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> getAssignedPatients(Long doctorUserId) {
        Doctor doctor = doctorRepository.findByUser_Id(doctorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found for current user"));
        return patientRepository.findByAssignedDoctor_Id(doctor.getId()).stream().map(mapper::toPatientResponse).toList();
    }

    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (userRepository.existsByPhone(request.phone())) {
            throw new DuplicateResourceException("Phone already exists");
        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .phone(request.phone())
            .password(passwordEncoder.encode(request.password() == null || request.password().isBlank() ? "Patient@123" : request.password()))
                .role(Role.PATIENT)
                .active(true)
                .build();
        user = userRepository.save(user);

        Doctor assignedDoctor = null;
        if (request.assignedDoctorId() != null) {
            assignedDoctor = doctorRepository.findById(request.assignedDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.assignedDoctorId()));
        }

        Patient patient = Patient.builder()
                .user(user)
                .dateOfBirth(request.dateOfBirth())
                .gender(request.gender())
                .bloodGroup(request.bloodGroup())
                .address(request.address())
                .emergencyContact(request.emergencyContact())
                .assignedDoctor(assignedDoctor)
                .build();
        return mapper.toPatientResponse(patientRepository.save(patient));
    }

    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = findPatient(id);
        User user = patient.getUser();
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        userRepository.save(user);

        patient.setDateOfBirth(request.dateOfBirth());
        patient.setGender(request.gender());
        patient.setBloodGroup(request.bloodGroup());
        patient.setAddress(request.address());
        patient.setEmergencyContact(request.emergencyContact());
        if (request.assignedDoctorId() != null) {
            patient.setAssignedDoctor(doctorRepository.findById(request.assignedDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.assignedDoctorId())));
        }
        return mapper.toPatientResponse(patientRepository.save(patient));
    }

    @Transactional
    public void deletePatient(Long id) {
        Patient patient = findPatient(id);
        patientRepository.delete(patient);
        userRepository.delete(patient.getUser());
    }

    private Patient findPatient(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }
}