package com.deliverymatch.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trips")
@EntityListeners(AuditingEntityListener.class)
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @Column(nullable = false)
    private String departureLocation;

    @Column(nullable = false)
    private String destinationLocation;

    @ElementCollection
    @CollectionTable(name = "trip_intermediate_stops", joinColumns = @JoinColumn(name = "trip_id"))
    @Column(name = "stop_location")
    private List<String> intermediateStops = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime estimatedArrivalTime;

    @Column(nullable = false)
    private Double maxLength;

    @Column(nullable = false)
    private Double maxWidth;

    @Column(nullable = false)
    private Double maxHeight;

    @Column(nullable = false)
    private Double maxWeight;

    @Column(nullable = false)
    private Double availableCapacity;

    @ElementCollection
    @CollectionTable(name = "trip_accepted_cargo_types", joinColumns = @JoinColumn(name = "trip_id"))
    @Column(name = "cargo_type")
    private List<String> acceptedCargoTypes = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status = TripStatus.ACTIVE;

    @Column(nullable = false)
    private Double price;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ShipmentRequest> shipmentRequests = new ArrayList<>();

    public enum TripStatus {
        ACTIVE,     // Available for requests
        IN_PROGRESS, // Trip has started
        COMPLETED,   // Trip finished
        CANCELLED    // Trip cancelled
    }
} 