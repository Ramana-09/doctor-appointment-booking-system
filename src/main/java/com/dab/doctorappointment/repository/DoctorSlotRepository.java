package com.dab.doctorappointment.repository;

import com.dab.doctorappointment.entity.DoctorSlot;
import com.dab.doctorappointment.entity.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DoctorSlotRepository extends JpaRepository<DoctorSlot, Long> {

    List<DoctorSlot> findByDoctorIdAndStatus(Long doctorId, SlotStatus status);

    // Scheduler idha use pண்ணும் — expired locks find pண்ண
    List<DoctorSlot> findByStatusAndLockedAtBefore(SlotStatus status, LocalDateTime time);
}
