package com.deliverymatch.service;

import com.deliverymatch.dto.ShipmentRequestResponse;
import com.deliverymatch.dto.UpdateShipmentRequestStatusRequest;
import com.deliverymatch.entity.ShipmentRequest;
import com.deliverymatch.entity.Trip;
import com.deliverymatch.entity.User;
import com.deliverymatch.exception.ResourceNotFoundException;
import com.deliverymatch.exception.UnauthorizedException;
import com.deliverymatch.repository.ShipmentRequestRepository;
import com.deliverymatch.repository.TripRepository;
import com.deliverymatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShipmentRequestService {
    
    private final ShipmentRequestRepository shipmentRequestRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    
    public Page<ShipmentRequestResponse> getRequestsForDriver(Long driverId, Pageable pageable) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        
        Page<ShipmentRequest> requests = shipmentRequestRepository.findPendingRequestsForDriver(driver, pageable);
        return requests.map(this::mapToShipmentRequestResponse);
    }
    
    public Page<ShipmentRequestResponse> getAcceptedRequestsForDriver(Long driverId, Pageable pageable) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        
        Page<ShipmentRequest> requests = shipmentRequestRepository.findAcceptedRequestsForDriver(driver, pageable);
        return requests.map(this::mapToShipmentRequestResponse);
    }
    
    public List<ShipmentRequestResponse> getCompletedRequestsForDriver(Long driverId) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        
        List<ShipmentRequest> requests = shipmentRequestRepository.findCompletedRequestsForDriver(driver);
        return requests.stream().map(this::mapToShipmentRequestResponse).collect(Collectors.toList());
    }
    
    public Page<ShipmentRequestResponse> getRequestsForTrip(Long tripId, Long driverId, Pageable pageable) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        
        if (!trip.getDriver().getId().equals(driverId)) {
            throw new UnauthorizedException("Only the trip driver can view requests");
        }
        
        Page<ShipmentRequest> requests = shipmentRequestRepository.findByTripOrderByCreatedAtDesc(trip, pageable);
        return requests.map(this::mapToShipmentRequestResponse);
    }
    
    public ShipmentRequestResponse updateRequestStatus(Long requestId, Long driverId, UpdateShipmentRequestStatusRequest request) {
        ShipmentRequest shipmentRequest = shipmentRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment request not found"));
        
        if (!shipmentRequest.getTrip().getDriver().getId().equals(driverId)) {
            throw new UnauthorizedException("Only the trip driver can update request status");
        }
        
        if (request.getStatus() == ShipmentRequest.RequestStatus.REJECTED && 
            (request.getRejectionReason() == null || request.getRejectionReason().trim().isEmpty())) {
            throw new IllegalArgumentException("Rejection reason is required when rejecting a request");
        }
        
        shipmentRequest.setStatus(request.getStatus());
        if (request.getStatus() == ShipmentRequest.RequestStatus.REJECTED) {
            shipmentRequest.setRejectionReason(request.getRejectionReason());
        }
        
        ShipmentRequest updatedRequest = shipmentRequestRepository.save(shipmentRequest);
        return mapToShipmentRequestResponse(updatedRequest);
    }
    
    public ShipmentRequestResponse getRequestById(Long requestId, Long driverId) {
        ShipmentRequest request = shipmentRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment request not found"));
        
        if (!request.getTrip().getDriver().getId().equals(driverId)) {
            throw new UnauthorizedException("Access denied");
        }
        
        return mapToShipmentRequestResponse(request);
    }
    
    private ShipmentRequestResponse mapToShipmentRequestResponse(ShipmentRequest request) {
        return ShipmentRequestResponse.builder()
                .id(request.getId())
                .shipperId(request.getShipper().getId())
                .shipperName(request.getShipper().getFirstName() + " " + request.getShipper().getLastName())
                .shipperEmail(request.getShipper().getEmail())
                .tripId(request.getTrip().getId())
                .pickupLocation(request.getPickupLocation())
                .deliveryLocation(request.getDeliveryLocation())
                .pickupTime(request.getPickupTime())
                .deliveryDeadline(request.getDeliveryDeadline())
                .length(request.getLength())
                .width(request.getWidth())
                .height(request.getHeight())
                .weight(request.getWeight())
                .cargoType(request.getCargoType())
                .description(request.getDescription())
                .offeredPrice(request.getOfferedPrice())
                .status(request.getStatus())
                .rejectionReason(request.getRejectionReason())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }
} 