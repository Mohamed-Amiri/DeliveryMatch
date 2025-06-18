package com.deliverymatch.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "driver_profiles")
@EntityListeners(AuditingEntityListener.class)
public class DriverProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String licenseNumber;

    @Column(nullable = false)
    private String vehicleType;

    @Column(nullable = false)
    private String vehicleNumber;

    private String vehicleModel;
    private String vehicleColor;
    private Integer vehicleCapacity;
    private String insuranceNumber;
    private String insuranceProvider;
    private LocalDateTime insuranceExpiryDate;
    private String licenseExpiryDate;
    private String currentLocation;
    private Boolean isAvailable;
    private Double rating;
    private Integer totalDeliveries;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
} 