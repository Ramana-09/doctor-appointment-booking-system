package com.dab.doctorappointment.dto;

import com.dab.doctorappointment.entity.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private Long id;
    private String doctorName;
    private String specialization;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private AppointmentStatus status;
    private LocalDateTime bookedAt;
    private String reasonForVisit;
}
