package com.dab.doctorappointment.dto;

import com.dab.doctorappointment.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Role is required")
    private Role role; // PATIENT, DOCTOR, ADMIN

    // Optional — only needed if role = DOCTOR
    private String specialization;
    private Integer experience;
    private String clinicAddress;
}