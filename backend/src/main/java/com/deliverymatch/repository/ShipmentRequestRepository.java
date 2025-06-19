package com.deliverymatch.repository;

import com.deliverymatch.entity.ShipmentRequest;
import com.deliverymatch.entity.Trip;
import com.deliverymatch.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentRequestRepository extends JpaRepository<ShipmentRequest, Long> {
    
    // Find requests by shipper
    Page<ShipmentRequest> findByShipperOrderByCreatedAtDesc(User shipper, Pageable pageable);
    
    // Find requests by shipper and status
    List<ShipmentRequest> findByShipperAndStatusOrderByCreatedAtDesc(User shipper, ShipmentRequest.RequestStatus status);
    
    // Find requests by trip
    Page<ShipmentRequest> findByTripOrderByCreatedAtDesc(Trip trip, Pageable pageable);
    
    // Find requests by trip and status
    List<ShipmentRequest> findByTripAndStatusOrderByCreatedAtDesc(Trip trip, ShipmentRequest.RequestStatus status);
    
    // Count requests by trip and status
    long countByTripAndStatus(Trip trip, ShipmentRequest.RequestStatus status);
    
    // Find pending requests for a driver's trips
    @Query("SELECT sr FROM ShipmentRequest sr WHERE sr.trip.driver = :driver AND sr.status = 'PENDING' ORDER BY sr.createdAt DESC")
    Page<ShipmentRequest> findPendingRequestsForDriver(@Param("driver") User driver, Pageable pageable);
    
    // Find accepted requests for a driver's trips
    @Query("SELECT sr FROM ShipmentRequest sr WHERE sr.trip.driver = :driver AND sr.status = 'ACCEPTED' ORDER BY sr.createdAt DESC")
    Page<ShipmentRequest> findAcceptedRequestsForDriver(@Param("driver") User driver, Pageable pageable);
    
    // Find completed requests for a driver's trips
    @Query("SELECT sr FROM ShipmentRequest sr WHERE sr.trip.driver = :driver AND sr.status = 'COMPLETED' ORDER BY sr.createdAt DESC")
    List<ShipmentRequest> findCompletedRequestsForDriver(@Param("driver") User driver);
} 