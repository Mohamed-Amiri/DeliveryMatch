package com.deliverymatch.controller;

import com.deliverymatch.dto.ShipmentRequest;
import com.deliverymatch.dto.ShipmentRequestResponse;
import com.deliverymatch.dto.TripResponse;
import com.deliverymatch.dto.TripSearchRequest;
import com.deliverymatch.service.ShipmentRequestService;
import com.deliverymatch.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipper")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ShipperController {
    
    private final TripService tripService;
    private final ShipmentRequestService shipmentRequestService;
    
    // Search available trips
    @PostMapping("/trips/search")
    public ResponseEntity<Page<TripResponse>> searchTrips(
            @RequestBody TripSearchRequest searchRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TripResponse> trips = tripService.searchTrips(searchRequest, pageable);
        return ResponseEntity.ok(trips);
    }
    
    // Get trip details
    @GetMapping("/trips/{tripId}")
    public ResponseEntity<TripResponse> getTripDetails(@PathVariable Long tripId) {
        TripResponse trip = tripService.getTripDetails(tripId);
        return ResponseEntity.ok(trip);
    }
    
    // Create shipment request
    @PostMapping("/requests")
    public ResponseEntity<ShipmentRequestResponse> createShipmentRequest(
            @RequestBody ShipmentRequest request,
            Authentication authentication) {
        String shipperEmail = authentication.getName();
        ShipmentRequestResponse response = shipmentRequestService.createRequest(request, shipperEmail);
        return ResponseEntity.ok(response);
    }
    
    // Get all shipment requests for the shipper
    @GetMapping("/requests")
    public ResponseEntity<Page<ShipmentRequestResponse>> getMyRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        String shipperEmail = authentication.getName();
        Pageable pageable = PageRequest.of(page, size);
        Page<ShipmentRequestResponse> requests = shipmentRequestService.getShipperRequests(shipperEmail, pageable);
        return ResponseEntity.ok(requests);
    }
    
    // Get shipment request details
    @GetMapping("/requests/{requestId}")
    public ResponseEntity<ShipmentRequestResponse> getRequestDetails(
            @PathVariable Long requestId,
            Authentication authentication) {
        String shipperEmail = authentication.getName();
        ShipmentRequestResponse request = shipmentRequestService.getRequestDetails(requestId, shipperEmail);
        return ResponseEntity.ok(request);
    }
    
    // Get completed shipments
    @GetMapping("/requests/completed")
    public ResponseEntity<List<ShipmentRequestResponse>> getCompletedShipments(Authentication authentication) {
        String shipperEmail = authentication.getName();
        List<ShipmentRequestResponse> completedShipments = shipmentRequestService.getCompletedShipments(shipperEmail);
        return ResponseEntity.ok(completedShipments);
    }
    
    // Cancel shipment request
    @PostMapping("/requests/{requestId}/cancel")
    public ResponseEntity<ShipmentRequestResponse> cancelRequest(
            @PathVariable Long requestId,
            Authentication authentication) {
        String shipperEmail = authentication.getName();
        ShipmentRequestResponse response = shipmentRequestService.cancelRequest(requestId, shipperEmail);
        return ResponseEntity.ok(response);
    }
} 