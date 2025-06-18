package com.deliverymatch.dto;

import com.deliverymatch.entity.ShipmentRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentRequestResponse {
    private Long id;
    private Long shipperId;
    private String shipperName;
    private String shipperEmail;
    private Long tripId;
    private String pickupLocation;
    private String deliveryLocation;
    private LocalDateTime pickupTime;
    private LocalDateTime deliveryDeadline;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal weight;
    private String cargoType;
    private String description;
    private BigDecimal offeredPrice;
    private ShipmentRequest.RequestStatus status;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 