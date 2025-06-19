package com.deliverymatch.service;

import com.deliverymatch.dto.ShipmentRequest;
import com.deliverymatch.dto.ShipmentRequestResponse;
import com.deliverymatch.dto.UpdateShipmentRequestStatusRequest;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShipmentRequestService {
    
    private final ShipmentRequestRepository shipmentRequestRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    
    public ShipmentRequestResponse createRequest(ShipmentRequest request, String shipperEmail) {
        User shipper = userRepository.findByEmail(shipperEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Shipper not found"));
        
        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        
        // Validate cargo dimensions and weight
        if ((request.getWeight() != null && trip.getMaxWeight() != null && request.getWeight() > trip.getMaxWeight()) ||
            (request.getLength() != null && trip.getMaxLength() != null && request.getLength() > trip.getMaxLength()) ||
            (request.getWidth() != null && trip.getMaxWidth() != null && request.getWidth() > trip.getMaxWidth()) ||
            (request.getHeight() != null && trip.getMaxHeight() != null && request.getHeight() > trip.getMaxHeight())) {
            throw new IllegalArgumentException("Cargo dimensions exceed trip capacity");
        }
        
        // Validate cargo type
        if (!trip.getAcceptedCargoTypes().contains(request.getCargoType())) {
            throw new IllegalArgumentException("Cargo type not accepted for this trip");
        }
        
        var shipmentRequest = com.deliverymatch.entity.ShipmentRequest.builder()
                .trip(trip)
                .shipper(shipper)
                .cargoType(request.getCargoType())
                .weight(request.getWeight() != null ? BigDecimal.valueOf(request.getWeight()) : null)
                .length(request.getLength() != null ? BigDecimal.valueOf(request.getLength()) : null)
                .width(request.getWidth() != null ? BigDecimal.valueOf(request.getWidth()) : null)
                .height(request.getHeight() != null ? BigDecimal.valueOf(request.getHeight()) : null)
                .description(request.getDescription())
                .specialInstructions(request.getSpecialInstructions())
                .fragile(request.getFragile())
                .insuranceRequired(request.getInsuranceRequired())
                .pickupLocation(request.getPickupLocation())
                .deliveryLocation(request.getDeliveryLocation())
                .pickupTime(request.getPickupTime())
                .deliveryDeadline(request.getDeliveryDeadline())
                .offeredPrice(request.getOfferedPrice() != null ? BigDecimal.valueOf(request.getOfferedPrice()) : null)
                .status(com.deliverymatch.entity.ShipmentRequest.RequestStatus.PENDING)
                .build();
        
        var savedRequest = shipmentRequestRepository.save(shipmentRequest);
        return mapToShipmentRequestResponse(savedRequest);
    }
    
    public Page<ShipmentRequestResponse> getShipperRequests(String shipperEmail, Pageable pageable) {
        User shipper = userRepository.findByEmail(shipperEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Shipper not found"));
        
        Page<com.deliverymatch.entity.ShipmentRequest> requests = 
            shipmentRequestRepository.findByShipperOrderByCreatedAtDesc(shipper, pageable);
        return requests.map(r -> mapToShipmentRequestResponse(r));
    }
    
    public ShipmentRequestResponse getRequestDetails(Long requestId, String shipperEmail) {
        com.deliverymatch.entity.ShipmentRequest request = shipmentRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        
        if (!request.getShipper().getEmail().equals(shipperEmail)) {
            throw new UnauthorizedException("Not authorized to view this request");
        }
        
        return mapToShipmentRequestResponse(request);
    }
    
    public List<ShipmentRequestResponse> getCompletedShipments(String shipperEmail) {
        User shipper = userRepository.findByEmail(shipperEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Shipper not found"));
        
        List<com.deliverymatch.entity.ShipmentRequest> completedRequests = 
            shipmentRequestRepository.findByShipperAndStatusOrderByCreatedAtDesc(
                shipper, com.deliverymatch.entity.ShipmentRequest.RequestStatus.COMPLETED);
        
        return completedRequests.stream()
                .map(r -> mapToShipmentRequestResponse(r))
                .collect(Collectors.toList());
    }
    
    public ShipmentRequestResponse cancelRequest(Long requestId, String shipperEmail) {
        com.deliverymatch.entity.ShipmentRequest request = shipmentRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        
        if (!request.getShipper().getEmail().equals(shipperEmail)) {
            throw new UnauthorizedException("Not authorized to cancel this request");
        }
        
        if (request.getStatus() != com.deliverymatch.entity.ShipmentRequest.RequestStatus.PENDING) {
            throw new IllegalStateException("Can only cancel pending requests");
        }
        
        request.setStatus(com.deliverymatch.entity.ShipmentRequest.RequestStatus.CANCELLED);
        shipmentRequestRepository.save(request);
        
        return mapToShipmentRequestResponse(request);
    }
    
    public Page<ShipmentRequestResponse> getRequestsForDriver(Long driverId, Pageable pageable) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        
        Page<com.deliverymatch.entity.ShipmentRequest> requests = shipmentRequestRepository.findPendingRequestsForDriver(driver, pageable);
        return requests.map(r -> mapToShipmentRequestResponse(r));
    }
    
    public Page<ShipmentRequestResponse> getAcceptedRequestsForDriver(Long driverId, Pageable pageable) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        
        Page<com.deliverymatch.entity.ShipmentRequest> requests = shipmentRequestRepository.findAcceptedRequestsForDriver(driver, pageable);
        return requests.map(r -> mapToShipmentRequestResponse(r));
    }
    
    public List<ShipmentRequestResponse> getCompletedRequestsForDriver(Long driverId) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        
        List<com.deliverymatch.entity.ShipmentRequest> requests = shipmentRequestRepository.findCompletedRequestsForDriver(driver);
        return requests.stream().map(r -> mapToShipmentRequestResponse(r)).collect(Collectors.toList());
    }
    
    public Page<ShipmentRequestResponse> getRequestsForTrip(Long tripId, Long driverId, Pageable pageable) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        
        if (!trip.getDriver().getId().equals(driverId)) {
            throw new UnauthorizedException("Only the trip driver can view requests");
        }
        
        Page<com.deliverymatch.entity.ShipmentRequest> requests = shipmentRequestRepository.findByTripOrderByCreatedAtDesc(trip, pageable);
        return requests.map(r -> mapToShipmentRequestResponse(r));
    }
    
    public ShipmentRequestResponse updateRequestStatus(Long requestId, Long driverId, UpdateShipmentRequestStatusRequest request) {
        com.deliverymatch.entity.ShipmentRequest shipmentRequest = shipmentRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment request not found"));
        
        if (!shipmentRequest.getTrip().getDriver().getId().equals(driverId)) {
            throw new UnauthorizedException("Only the trip driver can update request status");
        }
        
        if (request.getStatus() == com.deliverymatch.entity.ShipmentRequest.RequestStatus.REJECTED && 
            (request.getRejectionReason() == null || request.getRejectionReason().trim().isEmpty())) {
            throw new IllegalArgumentException("Rejection reason is required when rejecting a request");
        }
        
        shipmentRequest.setStatus(request.getStatus());
        if (request.getStatus() == com.deliverymatch.entity.ShipmentRequest.RequestStatus.REJECTED) {
            shipmentRequest.setRejectionReason(request.getRejectionReason());
        }
        
        com.deliverymatch.entity.ShipmentRequest updatedRequest = shipmentRequestRepository.save(shipmentRequest);
        return mapToShipmentRequestResponse(updatedRequest);
    }
    
    public ShipmentRequestResponse getRequestById(Long requestId, Long driverId) {
        com.deliverymatch.entity.ShipmentRequest request = shipmentRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment request not found"));
        
        if (!request.getTrip().getDriver().getId().equals(driverId)) {
            throw new UnauthorizedException("Access denied");
        }
        
        return mapToShipmentRequestResponse(request);
    }
    
    private ShipmentRequestResponse mapToShipmentRequestResponse(com.deliverymatch.entity.ShipmentRequest request) {
        return ShipmentRequestResponse.builder()
                .id(request.getId())
                .tripId(request.getTrip().getId())
                .shipperId(request.getShipper().getId())
                .shipperName(request.getShipper().getFirstName() + " " + request.getShipper().getLastName())
                .cargoType(request.getCargoType())
                .weight(request.getWeight() != null ? request.getWeight().doubleValue() : null)
                .length(request.getLength() != null ? request.getLength().doubleValue() : null)
                .width(request.getWidth() != null ? request.getWidth().doubleValue() : null)
                .height(request.getHeight() != null ? request.getHeight().doubleValue() : null)
                .description(request.getDescription())
                .specialInstructions(request.getSpecialInstructions())
                .fragile(request.getFragile())
                .insuranceRequired(request.getInsuranceRequired())
                .pickupLocation(request.getPickupLocation())
                .deliveryLocation(request.getDeliveryLocation())
                .pickupTime(request.getPickupTime())
                .deliveryDeadline(request.getDeliveryDeadline())
                .offeredPrice(request.getOfferedPrice() != null ? request.getOfferedPrice().doubleValue() : null)
                .status(request.getStatus() != null ? request.getStatus().toString() : null)
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }
} 