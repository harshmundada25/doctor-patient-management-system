package com.hospital.management.service;

import com.hospital.management.dto.appointment.AppointmentRequest;
import com.hospital.management.dto.appointment.AppointmentResponse;
import com.hospital.management.entity.Appointment;
import com.hospital.management.entity.AppointmentStatus;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.security.UserPrincipal;
import com.hospital.management.util.DomainMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DomainMapper mapper;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, DomainMapper mapper) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(mapper::toAppointmentResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsForDoctor(Long doctorId) {
        return appointmentRepository.findByDoctor_Id(doctorId).stream().map(mapper::toAppointmentResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsForPatient(Long patientId) {
        return appointmentRepository.findByPatient_Id(patientId).stream().map(mapper::toAppointmentResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getMyAppointments(UserPrincipal principal) {
        if (principal.getRole().name().equals("DOCTOR")) {
            Doctor doctor = doctorRepository.findByUser_Id(principal.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));
            return getAppointmentsForDoctor(doctor.getId());
        }
        Patient patient = patientRepository.findByUser_Id(principal.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
        return getAppointmentsForPatient(patient.getId());
    }

    @Transactional
    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.patientId()));
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.doctorId()));

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDateTime(request.appointmentDateTime())
                .reason(request.reason())
                .status(request.status() == null ? AppointmentStatus.PENDING : request.status())
                .notes(request.notes())
                .build();
        // If patient is not yet assigned to a primary doctor, set the assigned doctor when booking
        if (patient.getAssignedDoctor() == null || !patient.getAssignedDoctor().getId().equals(doctor.getId())) {
            patient.setAssignedDoctor(doctor);
            patientRepository.save(patient);
        }

        return mapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentResponse updateAppointment(Long id, AppointmentRequest request) {
        Appointment appointment = findAppointment(id);
        if (request.patientId() != null) {
            appointment.setPatient(patientRepository.findById(request.patientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.patientId())));
        }
        if (request.doctorId() != null) {
            appointment.setDoctor(doctorRepository.findById(request.doctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.doctorId())));
        }
        if (request.appointmentDateTime() != null) {
            appointment.setAppointmentDateTime(request.appointmentDateTime());
        }
        if (request.reason() != null) {
            appointment.setReason(request.reason());
        }
        if (request.status() != null) {
            appointment.setStatus(request.status());
        }
        appointment.setNotes(request.notes());
        return mapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentResponse updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = findAppointment(id);
        appointment.setStatus(status);
        return mapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public void deleteAppointment(Long id) {
        appointmentRepository.delete(findAppointment(id));
    }

    private Appointment findAppointment(Long id) {
        return appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
    }
}