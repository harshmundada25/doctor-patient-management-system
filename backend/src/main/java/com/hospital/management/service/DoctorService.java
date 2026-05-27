package com.hospital.management.service;

import com.hospital.management.dto.doctor.DoctorRequest;
import com.hospital.management.dto.doctor.DoctorResponse;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Role;
import com.hospital.management.entity.User;
import com.hospital.management.exception.DuplicateResourceException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.util.DomainMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DomainMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepository, UserRepository userRepository, DomainMapper mapper, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream().map(mapper::toDoctorResponse).toList();
    }

    @Transactional(readOnly = true)
    public DoctorResponse getDoctorById(Long id) {
        return mapper.toDoctorResponse(findDoctor(id));
    }

    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (userRepository.existsByPhone(request.phone())) {
            throw new DuplicateResourceException("Phone already exists");
        }
        if (doctorRepository.existsByLicenseNumber(request.licenseNumber())) {
            throw new DuplicateResourceException("License number already exists");
        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .phone(request.phone())
            .password(passwordEncoder.encode(request.password() == null || request.password().isBlank() ? "Doctor@123" : request.password()))
                .role(Role.DOCTOR)
                .active(true)
                .build();
        user = userRepository.save(user);

        Doctor doctor = Doctor.builder()
                .user(user)
                .specialty(request.specialty())
                .licenseNumber(request.licenseNumber())
                .yearsOfExperience(request.yearsOfExperience())
            .availability(request.availability())
                .active(request.active() == null || request.active())
                .build();
        return mapper.toDoctorResponse(doctorRepository.save(doctor));
    }

    @Transactional
    public DoctorResponse updateDoctor(Long id, DoctorRequest request) {
        Doctor doctor = findDoctor(id);
        User user = doctor.getUser();

        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        userRepository.save(user);

        doctor.setSpecialty(request.specialty());
        doctor.setLicenseNumber(request.licenseNumber());
        doctor.setYearsOfExperience(request.yearsOfExperience());
        doctor.setAvailability(request.availability());
        doctor.setActive(request.active() == null || request.active());
        return mapper.toDoctorResponse(doctorRepository.save(doctor));
    }

    @Transactional
    public void deleteDoctor(Long id) {
        Doctor doctor = findDoctor(id);
        doctorRepository.delete(doctor);
        userRepository.delete(doctor.getUser());
    }

    private Doctor findDoctor(Long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }
}