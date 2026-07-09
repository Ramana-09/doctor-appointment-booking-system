package com.dab.doctorappointment.controller;

import com.dab.doctorappointment.dto.DoctorResponse;
import com.dab.doctorappointment.dto.SlotRequest;
import com.dab.doctorappointment.dto.SlotResponse;
import com.dab.doctorappointment.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    // Public - patient/anyone ellorum paાkalam
    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // Public - oru doctor-oda available slots
    @GetMapping("/{doctorId}/slots")
    public ResponseEntity<List<SlotResponse>> getAvailableSlots(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorService.getAvailableSlots(doctorId));
    }

    // Protected - login pண்ணின doctor mattum than tha oda slot add pண்ணமுடியும்
    @PostMapping("/slots")
    public ResponseEntity<SlotResponse> addSlot(
            @Valid @RequestBody SlotRequest request,
            Authentication authentication
    ) {
        String doctorEmail = authentication.getName(); // JWT token la irundhu email edukum
        return ResponseEntity.ok(doctorService.addSlot(doctorEmail, request));
    }


}
