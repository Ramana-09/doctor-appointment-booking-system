package com.dab.doctorappointment.service;

import com.dab.doctorappointment.dto.DoctorResponse;
import com.dab.doctorappointment.dto.SlotRequest;
import com.dab.doctorappointment.dto.SlotResponse;
import com.dab.doctorappointment.entity.Doctor;
import com.dab.doctorappointment.entity.DoctorSlot;
import com.dab.doctorappointment.entity.SlotStatus;
import com.dab.doctorappointment.repository.DoctorRepository;
import com.dab.doctorappointment.repository.DoctorSlotRepository;
import com.dab.doctorappointment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorSlotRepository doctorSlotRepository;
    private final UserRepository userRepository;

    // All doctors list pண்ணும்
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctor -> new DoctorResponse(
                        doctor.getId(),
                        doctor.getUser().getName(),
                        doctor.getSpecialization(),
                        doctor.getExperience(),
                        doctor.getClinicAddress()
                ))
                .toList();
    }

    // Oru doctor-oda AVAILABLE slots mattum kondu varum
    public List<SlotResponse> getAvailableSlots(Long doctorId) {
        List<DoctorSlot> slots = doctorSlotRepository.findByDoctorIdAndStatus(doctorId, SlotStatus.AVAILABLE);

        return slots.stream()
                .map(slot -> new SlotResponse(
                        slot.getId(),
                        slot.getDate(),
                        slot.getStartTime(),
                        slot.getEndTime(),
                        slot.getStatus()
                ))
                .toList();
    }

    // Logged-in doctor (email vachi) oru pudhu slot add pண்ணும்
    public SlotResponse addSlot(String doctorEmail, SlotRequest request) {

        Doctor doctor = doctorRepository.findByUserId(
                userRepository.findByEmail(doctorEmail)
                        .orElseThrow(() -> new RuntimeException("User not found"))
                        .getId()
        ).orElseThrow(() -> new RuntimeException("Doctor profile not found for this user"));

        DoctorSlot slot = new DoctorSlot();
        slot.setDoctor(doctor);
        slot.setDate(request.getDate());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        slot.setStatus(SlotStatus.AVAILABLE);

        DoctorSlot saved = doctorSlotRepository.save(slot);

        return new SlotResponse(
                saved.getId(),
                saved.getDate(),
                saved.getStartTime(),
                saved.getEndTime(),
                saved.getStatus()
        );
    }
}
