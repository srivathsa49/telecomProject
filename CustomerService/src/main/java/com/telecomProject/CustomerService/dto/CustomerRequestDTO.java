package com.telecomProject.CustomerService.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CustomerRequestDTO {
    private String firstName;
    private String lastName;
    private String address;
    @Nullable
    @Email(message = "Invalid email address", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    private String email;
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;
    @NotNull
    private String planName; // 👈 not the whole Plan
    @FutureOrPresent(message = "Plan start time must be in the present or in the future")
    @NotNull
    private LocalDateTime planStartTime;
    @Future(message = "Plan end time must be in the future")
    @NotNull
    private LocalDateTime planEndTime;
}

