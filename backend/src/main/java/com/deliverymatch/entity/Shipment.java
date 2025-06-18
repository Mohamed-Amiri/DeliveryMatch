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
@Table(name = "shipments")
@EntityListeners(AuditingEntityListener.class)
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shipper_id", nullable = false)
    private User shipper;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;

    @Column(nullable = false)
    private String pickupLocation;

    @Column(nullable = false)
    private String deliveryLocation;

    @Column(nullable = false)
    private LocalDateTime pickupTime;

    @Column(nullable = false)
    private LocalDateTime deliveryTime;

    @Column(nullable = false)
    private BigDecimal weight;

    @Column(nullable = false)
    private String dimensions;

    @Column(nullable = false)
    private String cargoType;

    @Column(nullable = false)
    private String status; // PENDING, ASSIGNED, IN_TRANSIT, DELIVERED, CANCELLED

    private String specialInstructions;
    private BigDecimal price;
    private String paymentStatus;
    private String trackingNumber;
    private LocalDateTime actualPickupTime;
    private LocalDateTime actualDeliveryTime;
    private String deliveryProof;
    private String cancellationReason;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
} 