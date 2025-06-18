package com.deliverymatch.controller;

import com.deliverymatch.dto.CreateTripRequest;
import com.deliverymatch.dto.ShipmentRequestResponse;
import com.deliverymatch.dto.TripResponse;
import com.deliverymatch.dto.UpdateShipmentRequestStatusRequest;
import com.deliverymatch.entity.Trip;
import com.deliverymatch.service.ShipmentRequestService;
import com.deliverymatch.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DriverController {
    
    private final TripService tripService;
    private final ShipmentRequestService shipmentRequestService;
    
    // Trip Management
    
    @PostMapping("/trips")
    public ResponseEntity<TripResponse> createTrip(
            @RequestBody CreateTripRequest request,
            Authentication authentication) {
        Long driverId = Long.parseLong(authentication.getName());
        TripResponse trip = tripService.createTrip(driverId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(trip);
    }
    
    @GetMapping("/trips")
    public ResponseEntity<Page<TripResponse>> getDriverTrips(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        Long driverId = Long.parseLong(authentication.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<TripResponse> trips = tripService.getDriverTrips(driverId, pageable);
        return ResponseEntity.ok(trips);
    }
    
    @GetMapping("/trips/active")
    public ResponseEntity<List<TripResponse>> getDriverActiveTrips(Authentication authentication) {
        Long driverId = Long.parseLong(authentication.getName());
        List<TripResponse> trips = tripService.getDriverActiveTrips(driverId);
        return ResponseEntity.ok(trips);
    }
    
    @GetMapping("/trips/completed")
    public ResponseEntity<List<TripResponse>> getDriverCompletedTrips(Authentication authentication) {
        Long driverId = Long.parseLong(authentication.getName());
        List<TripResponse> trips = tripService.getDriverCompletedTrips(driverId);
        return ResponseEntity.ok(trips);
    }
    
    @GetMapping("/trips/{tripId}")
    public ResponseEntity<TripResponse> getTripById(
            @PathVariable Long tripId,
            Authentication authentication) {
        Long driverId = Long.parseLong(authentication.getName());
        TripResponse trip = tripService.getTripById(tripId, driverId);
        return ResponseEntity.ok(trip);
    }
    
    @PutMapping("/trips/{tripId}/status")
    public ResponseEntity<TripResponse> updateTripStatus(
            @PathVariable Long tripId,
            @RequestParam Trip.TripStatus status,
            Authentication authentication) {
        Long driverId = Long.parseLong(authentication.getName());
        TripResponse trip = tripService.updateTripStatus(tripId, driverId, status);
        return ResponseEntity.ok(trip);
    }
    
    @DeleteMapping("/trips/{tripId}")
    public ResponseEntity<Void> deleteTrip(
            @PathVariable Long tripId,
            Authentication authentication) {
        Long driverId = Long.parseLong(authentication.getName());
        tripService.deleteTrip(tripId, driverId);
        return ResponseEntity.noContent().build();
    }
    
    // Shipment Request Management
    
    @GetMapping("/requests")
    public ResponseEntity<Page<ShipmentRequestResponse>> getPendingRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        Long driverId = Long.parseLong(authentication.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<ShipmentRequestResponse> requests = shipmentRequestService.getRequestsForDriver(driverId, pageable);
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/requests/accepted")
    public ResponseEntity<Page<ShipmentRequestResponse>> getAcceptedRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        Long driverId = Long.parseLong(authentication.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<ShipmentRequestResponse> requests = shipmentRequestService.getAcceptedRequestsForDriver(driverId, pageable);
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/requests/completed")
    public ResponseEntity<List<ShipmentRequestResponse>> getCompletedRequests(Authentication authentication) {
        Long driverId = Long.parseLong(authentication.getName());
        List<ShipmentRequestResponse> requests = shipmentRequestService.getCompletedRequestsForDriver(driverId);
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/trips/{tripId}/requests")
    public ResponseEntity<Page<ShipmentRequestResponse>> getRequestsForTrip(
            @PathVariable Long tripId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        Long driverId = Long.parseLong(authentication.getName());
        Pageable pageable = PageRequest.of(page, size);
        Page<ShipmentRequestResponse> requests = shipmentRequestService.getRequestsForTrip(tripId, driverId, pageable);
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/requests/{requestId}")
    public ResponseEntity<ShipmentRequestResponse> getRequestById(
            @PathVariable Long requestId,
            Authentication authentication) {
        Long driverId = Long.parseLong(authentication.getName());
        ShipmentRequestResponse request = shipmentRequestService.getRequestById(requestId, driverId);
        return ResponseEntity.ok(request);
    }
    
    @PutMapping("/requests/{requestId}/status")
    public ResponseEntity<ShipmentRequestResponse> updateRequestStatus(
            @PathVariable Long requestId,
            @RequestBody UpdateShipmentRequestStatusRequest request,
            Authentication authentication) {
        Long driverId = Long.parseLong(authentication.getName());
        ShipmentRequestResponse updatedRequest = shipmentRequestService.updateRequestStatus(requestId, driverId, request);
        return ResponseEntity.ok(updatedRequest);
    }
} 