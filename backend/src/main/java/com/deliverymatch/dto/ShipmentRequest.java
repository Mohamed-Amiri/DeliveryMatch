package com.deliverymatch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentRequest {
    private Long tripId;
    private String cargoType;
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
    private String description;
    private String specialInstructions;
    private Boolean fragile;
    private String insuranceRequired;
    private String pickupLocation;
    private String deliveryLocation;
    private LocalDateTime pickupTime;
    private LocalDateTime deliveryDeadline;
    private Double offeredPrice;
} 