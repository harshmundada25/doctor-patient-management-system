package com.hospital.management.service;

import com.hospital.management.dto.auth.AuthResponse;
import com.hospital.management.dto.auth.LoginRequest;
import com.hospital.management.dto.auth.RegisterRequest;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.Gender;
import com.hospital.management.entity.Role;
import com.hospital.management.entity.User;
import com.hospital.management.exception.DuplicateResourceException;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.security.JwtService;
import com.hospital.management.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PatientRepository patientRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already exists");
        }
        if (userRepository.existsByPhone(request.phone())) {
            throw new DuplicateResourceException("Phone already exists");
        }

        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phone(request.phone())
                .role(Role.PATIENT)
                .active(true)
                .build();
        user = userRepository.save(user);

        Patient patient = Patient.builder()
                .user(user)
            .gender(Gender.OTHER)
            .bloodGroup("Unknown")
            .address("Not provided")
                .build();
        patientRepository.save(patient);

        UserPrincipal principal = UserPrincipal.from(user);
        return new AuthResponse(jwtService.generateToken(principal), "Bearer", user.getId(), user.getFullName(), user.getEmail(), user.getRole().name(), 24L * 60L * 60L * 1000L);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return new AuthResponse(jwtService.generateToken(principal), "Bearer", principal.getUserId(), principal.getFullName(), principal.getUsername(), principal.getRole().name(), 24L * 60L * 60L * 1000L);
    }
}