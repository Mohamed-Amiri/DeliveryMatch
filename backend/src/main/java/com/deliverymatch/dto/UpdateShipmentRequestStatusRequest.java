package com.deliverymatch.dto;

import com.deliverymatch.entity.ShipmentRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShipmentRequestStatusRequest {
    private ShipmentRequest.RequestStatus status;
    private String rejectionReason; // Required if status is REJECTED
} 