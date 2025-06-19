package com.deliverymatch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TripSearchRequest {
    private String departureLocation;
    private String destinationLocation;
    private LocalDateTime departureDate;
    private List<String> cargoTypes;
    private Double maxWeight;
    private Double maxLength;
    private Double maxWidth;
    private Double maxHeight;
} 