package com.telecomProject.CDR.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String phoneNumber;
    private Plan plan;
    private LocalDateTime planStartTime;
    private LocalDateTime planEndTime;
    private int balance = 200;
    private boolean active = true;
}
