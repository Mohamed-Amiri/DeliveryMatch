package com.deliverymatch.dto;

import com.deliverymatch.entity.Trip;
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
public class TripResponse {
    private Long id;
    private Long driverId;
    private String driverName;
    private String departureLocation;
    private String destinationLocation;
    private List<String> intermediateStops;
    private LocalDateTime departureTime;
    private LocalDateTime estimatedArrivalTime;
    private Double maxLength;
    private Double maxWidth;
    private Double maxHeight;
    private Double maxWeight;
    private Double availableCapacity;
    private List<String> acceptedCargoTypes;
    private String description;
    private Trip.TripStatus status;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer pendingRequestsCount;
    private Integer acceptedRequestsCount;
} 