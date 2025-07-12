package com.telecomProject.CustomerService.dto;

import lombok.Data;

@Data
public class UpdateBalanceRequest {
    private String phoneNumber;
    private int balance;
}
