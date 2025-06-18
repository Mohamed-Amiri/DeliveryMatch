package com.deliverymatch.dto;

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
public class CreateTripRequest {
    private String departureLocation;
    private String destinationLocation;
    private List<String> intermediateStops;
    private LocalDateTime departureTime;
    private LocalDateTime estimatedArrivalTime;
    private BigDecimal maxLength; // in cm
    private BigDecimal maxWidth; // in cm
    private BigDecimal maxHeight; // in cm
    private BigDecimal maxWeight; // in kg
    private BigDecimal availableCapacity; // in kg
    private List<String> acceptedCargoTypes;
    private String description;
    private BigDecimal price; // price per kg
} 