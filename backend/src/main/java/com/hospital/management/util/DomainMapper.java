package com.hospital.management.util;

import com.hospital.management.dto.appointment.AppointmentResponse;
import com.hospital.management.dto.dashboard.RecentAppointmentResponse;
import com.hospital.management.dto.doctor.DoctorResponse;
import com.hospital.management.dto.medicalrecord.MedicalRecordResponse;
import com.hospital.management.dto.patient.PatientResponse;
import com.hospital.management.dto.prescription.PrescriptionResponse;
import com.hospital.management.entity.Appointment;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.MedicalRecord;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.Prescription;
import org.springframework.stereotype.Component;

@Component
public class DomainMapper {

    public DoctorResponse toDoctorResponse(Doctor doctor) {
        return new DoctorResponse(
                doctor.getId(),
                doctor.getUser().getId(),
                doctor.getUser().getFullName(),
                doctor.getUser().getEmail(),
                doctor.getUser().getPhone(),
                doctor.getSpecialty(),
                doctor.getLicenseNumber(),
                doctor.getYearsOfExperience(),
                doctor.getAvailability(),
                doctor.isActive()
        );
    }

    public PatientResponse toPatientResponse(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getUser().getId(),
                patient.getUser().getFullName(),
                patient.getUser().getEmail(),
                patient.getUser().getPhone(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getBloodGroup(),
                patient.getAddress(),
                patient.getEmergencyContact(),
                patient.getAssignedDoctor() != null ? patient.getAssignedDoctor().getId() : null,
                patient.getAssignedDoctor() != null ? patient.getAssignedDoctor().getUser().getFullName() : null
        );
    }

    public AppointmentResponse toAppointmentResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getPatient().getUser().getFullName(),
                appointment.getDoctor().getId(),
                appointment.getDoctor().getUser().getFullName(),
                appointment.getAppointmentDateTime(),
                appointment.getReason(),
                appointment.getStatus(),
                appointment.getNotes()
        );
    }

    public RecentAppointmentResponse toRecentAppointmentResponse(Appointment appointment) {
        return new RecentAppointmentResponse(
                appointment.getId(),
                appointment.getPatient().getUser().getFullName(),
                appointment.getDoctor().getUser().getFullName(),
                appointment.getAppointmentDateTime(),
                appointment.getStatus()
        );
    }

    public PrescriptionResponse toPrescriptionResponse(Prescription prescription) {
        return new PrescriptionResponse(
                prescription.getId(),
                prescription.getPatient().getId(),
                prescription.getPatient().getUser().getFullName(),
                prescription.getDoctor().getId(),
                prescription.getDoctor().getUser().getFullName(),
                prescription.getMedicationName(),
                prescription.getDosage(),
                prescription.getFrequency(),
                prescription.getDuration(),
                prescription.getNotes(),
                prescription.getPrescribedDate()
        );
    }

    public MedicalRecordResponse toMedicalRecordResponse(MedicalRecord record) {
        return new MedicalRecordResponse(
                record.getId(),
                record.getPatient().getId(),
                record.getPatient().getUser().getFullName(),
                record.getDoctor().getId(),
                record.getDoctor().getUser().getFullName(),
                record.getDiagnosis(),
                record.getTreatment(),
                record.getAllergies(),
                record.getNotes(),
                record.getVisitDate()
        );
    }
}