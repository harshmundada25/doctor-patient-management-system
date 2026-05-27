package com.hospital.management.controller;

import com.hospital.management.dto.auth.AuthResponse;
import com.hospital.management.dto.auth.LoginRequest;
import com.hospital.management.dto.auth.RegisterRequest;
import com.hospital.management.dto.auth.UserProfileResponse;
import com.hospital.management.security.UserPrincipal;
import com.hospital.management.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserProfileResponse me(@AuthenticationPrincipal UserPrincipal principal) {
        return new UserProfileResponse(
                principal.getUserId(),
                principal.getFullName(),
                principal.getUsername(),
                principal.getRole().name(),
                principal.isEnabled()
        );
    }
}