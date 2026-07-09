package com.dab.doctorappointment.repository;

import com.dab.doctorappointment.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
