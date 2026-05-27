package com.hospital.management.repository;

import com.hospital.management.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser_Id(Long userId);
    List<Patient> findByAssignedDoctor_Id(Long doctorId);
}