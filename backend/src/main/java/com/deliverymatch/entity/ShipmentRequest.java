package com.deliverymatch.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shipment_requests")
@EntityListeners(AuditingEntityListener.class)
public class ShipmentRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipper_id", nullable = false)
    private User shipper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(nullable = false)
    private String pickupLocation;

    @Column(nullable = false)
    private String deliveryLocation;

    @Column(nullable = false)
    private LocalDateTime pickupTime;

    @Column(nullable = false)
    private LocalDateTime deliveryDeadline;

    @Column(nullable = false)
    private BigDecimal length; // in cm

    @Column(nullable = false)
    private BigDecimal width; // in cm

    @Column(nullable = false)
    private BigDecimal height; // in cm

    @Column(nullable = false)
    private BigDecimal weight; // in kg

    @Column(nullable = false)
    private String cargoType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal offeredPrice; // price offered by shipper

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum RequestStatus {
        PENDING,    // Waiting for driver response
        ACCEPTED,   // Driver accepted the request
        REJECTED,   // Driver rejected the request
        CANCELLED,  // Shipper cancelled the request
        COMPLETED   // Shipment completed
    }
} 