package com.telecomProject.CustomerService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 *  planName
 *  * isInternational or NOT
 *  * details (readable, heavy, explainable)
 *  * ISD
 *  * LOCAL
 *  * STD
 *  * Offers
 *  * planType
 *  * talkTime (unlimited, etc)
 *  * dataPack (unlimited, etc)
 *  * SMS = default for all plan min 1000 per month
 * Benifits
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Column(unique = true, nullable = false)
    private String planName;
    private int price;
    private boolean isInternational;
    private String details;
    private int ISD;
    private int LOCAL;
    private int STD;
    private String offers;
    private String planType;
    private int talkTime;
    private int dataPack;
    private int SMS;
    private int validity;
}
