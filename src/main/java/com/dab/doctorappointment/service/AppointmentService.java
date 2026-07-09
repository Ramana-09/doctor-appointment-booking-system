package com.dab.doctorappointment.service;

import com.dab.doctorappointment.dto.AppointmentResponse;
import com.dab.doctorappointment.dto.BookingRequest;
import com.dab.doctorappointment.entity.*;
import com.dab.doctorappointment.repository.AppointmentRepository;
import com.dab.doctorappointment.repository.DoctorSlotRepository;
import com.dab.doctorappointment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final DoctorSlotRepository doctorSlotRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    // STEP A: Patient "Book" click pண்ணுறாாru — slot-a temporary lock pண்ணும்
    @Transactional
    public String lockSlot(Long slotId) {

        DoctorSlot slot = doctorSlotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        // Idhு than race-condition protect pண்ணும் check
        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new RuntimeException("Slot is not available for booking. Someone else may have already locked/booked it.");
        }

        slot.setStatus(SlotStatus.LOCKED);
        slot.setLockedAt(LocalDateTime.now());
        doctorSlotRepository.save(slot);

        return "Slot locked successfully. Please confirm within 5 minutes.";
    }

    // STEP B: Patient confirm pண்ணுறாாru — appointment create aagும்
    @Transactional
    public AppointmentResponse confirmBooking(Long slotId, String patientEmail, BookingRequest request) {

        DoctorSlot slot = doctorSlotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (slot.getStatus() != SlotStatus.LOCKED) {
            throw new RuntimeException("Slot is not locked. Please lock the slot first before confirming.");
        }

        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        slot.setStatus(SlotStatus.BOOKED);
        doctorSlotRepository.save(slot);

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctorSlot(slot);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setBookedAt(LocalDateTime.now());
        appointment.setReasonForVisit(request.getReasonForVisit());

        Appointment saved = appointmentRepository.save(appointment);

        return mapToResponse(saved);
    }

    // Patient's booking history
    public List<AppointmentResponse> getMyAppointments(String patientEmail) {
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return appointmentRepository.findByPatientId(patient.getId()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Cancel pண்ணும்போது slot-a thirumba AVAILABLE-ku maathanum
    @Transactional
    public String cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        DoctorSlot slot = appointment.getDoctorSlot();
        slot.setStatus(SlotStatus.AVAILABLE);
        slot.setLockedAt(null);
        doctorSlotRepository.save(slot);

        return "Appointment cancelled successfully.";
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        DoctorSlot slot = appointment.getDoctorSlot();
        return new AppointmentResponse(
                appointment.getId(),
                slot.getDoctor().getUser().getName(),
                slot.getDoctor().getSpecialization(),
                slot.getDate(),
                slot.getStartTime(),
                slot.getEndTime(),
                appointment.getStatus(),
                appointment.getBookedAt(),
                appointment.getReasonForVisit()
        );
    }
}
