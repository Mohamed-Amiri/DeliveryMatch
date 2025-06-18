package com.deliverymatch.dto;

import com.deliverymatch.entity.Trip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripResponse {
    private Long id;
    private Long driverId;
    private String driverName;
    private String departureLocation;
    private String destinationLocation;
    private List<String> intermediateStops;
    private LocalDateTime departureTime;
    private LocalDateTime estimatedArrivalTime;
    private BigDecimal maxLength;
    private BigDecimal maxWidth;
    private BigDecimal maxHeight;
    private BigDecimal maxWeight;
    private BigDecimal availableCapacity;
    private List<String> acceptedCargoTypes;
    private String description;
    private Trip.TripStatus status;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int pendingRequestsCount;
    private int acceptedRequestsCount;
} 