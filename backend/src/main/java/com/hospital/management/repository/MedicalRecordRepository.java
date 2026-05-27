package com.hospital.management.repository;

import com.hospital.management.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatient_Id(Long patientId);
    List<MedicalRecord> findByDoctor_Id(Long doctorId);
}