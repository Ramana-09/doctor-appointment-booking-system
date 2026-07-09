package com.dab.doctorappointment.repository;

import com.dab.doctorappointment.entity.Appointment;
import com.dab.doctorappointment.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByStatus(AppointmentStatus status);
}