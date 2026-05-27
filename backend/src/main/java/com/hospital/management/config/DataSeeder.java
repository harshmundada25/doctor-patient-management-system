package com.hospital.management.config;

import com.hospital.management.entity.Appointment;
import com.hospital.management.entity.AppointmentStatus;
import com.hospital.management.entity.Doctor;
import com.hospital.management.entity.Gender;
import com.hospital.management.entity.MedicalRecord;
import com.hospital.management.entity.Patient;
import com.hospital.management.entity.Prescription;
import com.hospital.management.entity.Role;
import com.hospital.management.entity.User;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.MedicalRecordRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.PrescriptionRepository;
import com.hospital.management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      DoctorRepository doctorRepository,
                      PatientRepository patientRepository,
                      AppointmentRepository appointmentRepository,
                      PrescriptionRepository prescriptionRepository,
                      MedicalRecordRepository medicalRecordRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        userRepository.save(createUser("System Admin", "admin@hospital.com", "9999999999", "Admin@123", Role.ADMIN));

        Doctor sarahKhan = createDoctor(createUser("Dr. Sarah Khan", "sarah.khan@hospital.com", "8888888888", "Doctor@123", Role.DOCTOR),
                "Cardiology", "MED-CARD-1001", 12, "Mon-Fri 9:00 AM - 5:00 PM");
        Doctor michaelReed = createDoctor(createUser("Dr. Michael Reed", "michael.reed@hospital.com", "8888888889", "Doctor@123", Role.DOCTOR),
                "Orthopedics", "MED-ORTH-1002", 9, "Mon-Thu 10:00 AM - 4:00 PM");
        Doctor priyaNair = createDoctor(createUser("Dr. Priya Nair", "priya.nair@hospital.com", "8888888890", "Doctor@123", Role.DOCTOR),
                "Dermatology", "MED-DERM-1003", 8, "Tue-Fri 11:00 AM - 6:00 PM");
        Doctor danielOrtiz = createDoctor(createUser("Dr. Daniel Ortiz", "daniel.ortiz@hospital.com", "8888888891", "Doctor@123", Role.DOCTOR),
                "Pediatrics", "MED-PED-1004", 11, "Mon-Sat 8:30 AM - 3:30 PM");

        Patient johnMathews = createPatient(createUser("John Mathews", "john.mathews@hospital.com", "7777777777", "Patient@123", Role.PATIENT),
                sarahKhan, LocalDate.of(1994, 5, 12), Gender.MALE, "O+", "12 Main Street, Austin, TX", "Mary Mathews - 555-1200");
        Patient aishaVerma = createPatient(createUser("Aisha Verma", "aisha.verma@hospital.com", "7777777778", "Patient@123", Role.PATIENT),
                michaelReed, LocalDate.of(1988, 11, 3), Gender.FEMALE, "A+", "44 Lakeview Drive, Dallas, TX", "Raj Verma - 555-2201");
        Patient lucasBrown = createPatient(createUser("Lucas Brown", "lucas.brown@hospital.com", "7777777779", "Patient@123", Role.PATIENT),
                priyaNair, LocalDate.of(2001, 2, 18), Gender.MALE, "B+", "109 Cedar Avenue, Denver, CO", "Olivia Brown - 555-3302");
        Patient sophiaPatel = createPatient(createUser("Sophia Patel", "sophia.patel@hospital.com", "7777777780", "Patient@123", Role.PATIENT),
                danielOrtiz, LocalDate.of(1999, 8, 27), Gender.FEMALE, "AB+", "78 Market Street, Seattle, WA", "Neha Patel - 555-4403");

        seedClinicalData(johnMathews, sarahKhan,
                "Routine cardiac checkup",
                AppointmentStatus.CONFIRMED,
                "Patient has mild shortness of breath",
                "Atorvastatin",
                "10mg",
                "Once daily",
                "30 days",
                "Take after dinner",
                "Hypertension",
                "Monitor BP, low-sodium diet, exercise",
                "Penicillin");

        seedClinicalData(aishaVerma, michaelReed,
                "Knee pain follow-up",
                AppointmentStatus.PENDING,
                "Reviewing recovery progress after physiotherapy",
                "Ibuprofen",
                "400mg",
                "Twice daily",
                "14 days",
                "Take with food",
                "Mild ligament strain",
                "Rest, ice packs, and guided physiotherapy",
                "None reported");

        seedClinicalData(lucasBrown, priyaNair,
                "Skin allergy consultation",
                AppointmentStatus.CONFIRMED,
                "Seasonal rash around arms and neck",
                "Cetirizine",
                "5mg",
                "Once daily",
                "10 days",
                "Preferably at night",
                "Allergic dermatitis",
                "Topical cream, avoid known triggers",
                "Dust pollen");

        seedClinicalData(sophiaPatel, danielOrtiz,
                "Pediatric wellness review",
                AppointmentStatus.COMPLETED,
                "Annual growth and nutrition check",
                "Vitamin D Drops",
                "400 IU",
                "Once daily",
                "60 days",
                "With breakfast",
                "Routine wellness check",
                "Balanced diet and active play",
                "None reported");
    }

    private User createUser(String fullName, String email, String phone, String rawPassword, Role role) {
        return userRepository.save(User.builder()
                .fullName(fullName)
                .email(email)
                .phone(phone)
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .active(true)
                .build());
    }

        private Doctor createDoctor(User user, String specialty, String licenseNumber, Integer yearsOfExperience, String availability) {
        return doctorRepository.save(Doctor.builder()
                .user(user)
                .specialty(specialty)
                .licenseNumber(licenseNumber)
                .yearsOfExperience(yearsOfExperience)
                                .availability(availability)
                .active(true)
                .build());
    }

    private Patient createPatient(User user,
                                  Doctor assignedDoctor,
                                  LocalDate dateOfBirth,
                                  Gender gender,
                                  String bloodGroup,
                                  String address,
                                  String emergencyContact) {
        return patientRepository.save(Patient.builder()
                .user(user)
                .dateOfBirth(dateOfBirth)
                .gender(gender)
                .bloodGroup(bloodGroup)
                .address(address)
                .emergencyContact(emergencyContact)
                .assignedDoctor(assignedDoctor)
                .build());
    }

    private void seedClinicalData(Patient patient,
                                  Doctor doctor,
                                  String appointmentReason,
                                  AppointmentStatus appointmentStatus,
                                  String appointmentNotes,
                                  String medicationName,
                                  String dosage,
                                  String frequency,
                                  String duration,
                                  String prescriptionNotes,
                                  String diagnosis,
                                  String treatment,
                                  String allergies) {
        appointmentRepository.save(Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDateTime(LocalDateTime.now().plusDays(1 + patient.getId()))
                .reason(appointmentReason)
                .status(appointmentStatus)
                .notes(appointmentNotes)
                .build());

        prescriptionRepository.save(Prescription.builder()
                .patient(patient)
                .doctor(doctor)
                .medicationName(medicationName)
                .dosage(dosage)
                .frequency(frequency)
                .duration(duration)
                .notes(prescriptionNotes)
                .prescribedDate(LocalDate.now())
                .build());

        medicalRecordRepository.save(MedicalRecord.builder()
                .patient(patient)
                .doctor(doctor)
                .diagnosis(diagnosis)
                .treatment(treatment)
                .allergies(allergies)
                .notes("Seeded showcase data")
                .visitDate(LocalDateTime.now())
                .build());
    }
}