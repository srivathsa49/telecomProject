package com.telecomProject.CustomerService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    @Column(unique = true, nullable = false)
    private String  phoneNumber;
    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;
    @Column(nullable = false)
    private LocalDateTime planStartTime;
    @Column(nullable = false)
    private LocalDateTime planEndTime;
    private int balance;
    private boolean active = true;
}
