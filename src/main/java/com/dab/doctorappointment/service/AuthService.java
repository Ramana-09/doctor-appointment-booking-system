package com.dab.doctorappointment.service;

import com.dab.doctorappointment.config.JwtService;
import com.dab.doctorappointment.dto.AuthResponse;
import com.dab.doctorappointment.dto.LoginRequest;
import com.dab.doctorappointment.dto.RegisterRequest;
import com.dab.doctorappointment.entity.Doctor;
import com.dab.doctorappointment.entity.Role;
import com.dab.doctorappointment.entity.User;
import com.dab.doctorappointment.repository.DoctorRepository;
import com.dab.doctorappointment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);

        // Role DOCTOR na, Doctor profile um create pண்ணனum
        if (request.getRole() == Role.DOCTOR) {
            Doctor doctor = new Doctor();
            doctor.setUser(savedUser);
            doctor.setSpecialization(request.getSpecialization());
            doctor.setExperience(request.getExperience());
            doctor.setClinicAddress(request.getClinicAddress());
            doctorRepository.save(doctor);
        }

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(savedUser.getEmail())
                        .password(savedUser.getPassword())
                        .authorities(java.util.List.of())
                        .build()
        );

        return new AuthResponse(token, savedUser.getRole().name(), savedUser.getName(), savedUser.getEmail());
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(java.util.List.of())
                .build();

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, user.getRole().name(), user.getName(), user.getEmail());
    }
}
