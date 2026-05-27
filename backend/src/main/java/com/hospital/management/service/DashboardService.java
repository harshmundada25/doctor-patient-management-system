package com.hospital.management.service;

import com.hospital.management.dto.dashboard.DashboardStatsResponse;
import com.hospital.management.dto.dashboard.RecentAppointmentResponse;
import com.hospital.management.entity.AppointmentStatus;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Patient;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.MedicalRecordRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.PrescriptionRepository;
import com.hospital.management.security.UserPrincipal;
import com.hospital.management.util.DomainMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class DashboardService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final DomainMapper mapper;

    public DashboardService(DoctorRepository doctorRepository,
                            PatientRepository patientRepository,
                            AppointmentRepository appointmentRepository,
                            PrescriptionRepository prescriptionRepository,
                            MedicalRecordRepository medicalRecordRepository,
                            DomainMapper mapper) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public DashboardStatsResponse getStats(UserPrincipal principal) {
        long doctorCount = doctorRepository.count();
        long patientCount = patientRepository.count();
        long appointmentCount = appointmentRepository.count();
        long prescriptionCount = prescriptionRepository.count();
        long medicalRecordCount = medicalRecordRepository.count();
        long pendingCount = appointmentRepository.findAll().stream().filter(appointment -> appointment.getStatus() == AppointmentStatus.PENDING).count();
        long completedCount = appointmentRepository.findAll().stream().filter(appointment -> appointment.getStatus() == AppointmentStatus.COMPLETED).count();
        long cancelledCount = appointmentRepository.findAll().stream().filter(appointment -> appointment.getStatus() == AppointmentStatus.CANCELLED).count();

        List<RecentAppointmentResponse> recentAppointments = appointmentRepository.findAll().stream()
                .sorted(Comparator.comparing(app -> app.getAppointmentDateTime(), Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5)
                .map(mapper::toRecentAppointmentResponse)
                .toList();

        return new DashboardStatsResponse(
                principal.getRole().name(),
                doctorCount,
                patientCount,
                appointmentCount,
                prescriptionCount,
                medicalRecordCount,
                pendingCount,
                completedCount,
                cancelledCount,
                recentAppointments
        );
    }

    @Transactional(readOnly = true)
    public DashboardStatsResponse getDoctorStats(UserPrincipal principal) {
        Doctor doctor = doctorRepository.findByUser_Id(principal.getUserId()).orElse(null);
        long appointmentCount = doctor == null ? 0 : appointmentRepository.findByDoctor_Id(doctor.getId()).size();
        long prescriptionCount = doctor == null ? 0 : prescriptionRepository.findByDoctor_Id(doctor.getId()).size();
        long recordCount = doctor == null ? 0 : medicalRecordRepository.findByDoctor_Id(doctor.getId()).size();
        long patientCount = doctor == null ? 0 : patientRepository.findByAssignedDoctor_Id(doctor.getId()).size();

        List<RecentAppointmentResponse> recentAppointments = doctor == null ? List.of() : appointmentRepository.findByDoctor_Id(doctor.getId()).stream()
                .sorted(Comparator.comparing(app -> app.getAppointmentDateTime(), Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5)
                .map(mapper::toRecentAppointmentResponse)
                .toList();

        return new DashboardStatsResponse(principal.getRole().name(), 1, patientCount, appointmentCount, prescriptionCount, recordCount, 0, 0, 0, recentAppointments);
    }

    @Transactional(readOnly = true)
    public DashboardStatsResponse getPatientStats(UserPrincipal principal) {
        Patient patient = patientRepository.findByUser_Id(principal.getUserId()).orElse(null);
        long appointmentCount = patient == null ? 0 : appointmentRepository.findByPatient_Id(patient.getId()).size();
        long prescriptionCount = patient == null ? 0 : prescriptionRepository.findByPatient_Id(patient.getId()).size();
        long recordCount = patient == null ? 0 : medicalRecordRepository.findByPatient_Id(patient.getId()).size();

        List<RecentAppointmentResponse> recentAppointments = patient == null ? List.of() : appointmentRepository.findByPatient_Id(patient.getId()).stream()
                .sorted(Comparator.comparing(app -> app.getAppointmentDateTime(), Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5)
                .map(mapper::toRecentAppointmentResponse)
                .toList();

        return new DashboardStatsResponse(principal.getRole().name(), 0, 1, appointmentCount, prescriptionCount, recordCount, 0, 0, 0, recentAppointments);
    }
}