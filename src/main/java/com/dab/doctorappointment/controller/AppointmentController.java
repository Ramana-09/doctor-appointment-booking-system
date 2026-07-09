package com.dab.doctorappointment.controller;

import com.dab.doctorappointment.dto.AppointmentResponse;
import com.dab.doctorappointment.dto.BookingRequest;
import com.dab.doctorappointment.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/lock/{slotId}")
    public ResponseEntity<String> lockSlot(@PathVariable Long slotId) {
        return ResponseEntity.ok(appointmentService.lockSlot(slotId));
    }

    @PostMapping("/confirm/{slotId}")
    public ResponseEntity<AppointmentResponse> confirmBooking(
            @PathVariable Long slotId,
            @RequestBody BookingRequest request,
            Authentication authentication
    ) {
        String patientEmail = authentication.getName();
        return ResponseEntity.ok(appointmentService.confirmBooking(slotId, patientEmail, request));
    }

    @GetMapping("/my")
    public ResponseEntity<List<AppointmentResponse>> getMyAppointments(Authentication authentication) {
        String patientEmail = authentication.getName();
        return ResponseEntity.ok(appointmentService.getMyAppointments(patientEmail));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }
}
