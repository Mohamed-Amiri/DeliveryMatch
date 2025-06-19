package com.deliverymatch.service;

import com.deliverymatch.dto.CreateTripRequest;
import com.deliverymatch.dto.TripResponse;
import com.deliverymatch.dto.TripSearchRequest;
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
public class TripService {
    
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final ShipmentRequestRepository shipmentRequestRepository;
    
    public TripResponse createTrip(Long driverId, CreateTripRequest request) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        
        Trip trip = Trip.builder()
                .driver(driver)
                .departureLocation(request.getDepartureLocation())
                .destinationLocation(request.getDestinationLocation())
                .intermediateStops(request.getIntermediateStops() != null ? request.getIntermediateStops() : List.of())
                .departureTime(request.getDepartureTime())
                .estimatedArrivalTime(request.getEstimatedArrivalTime())
                .maxLength(request.getMaxLength() != null ? request.getMaxLength().doubleValue() : null)
                .maxWidth(request.getMaxWidth() != null ? request.getMaxWidth().doubleValue() : null)
                .maxHeight(request.getMaxHeight() != null ? request.getMaxHeight().doubleValue() : null)
                .maxWeight(request.getMaxWeight() != null ? request.getMaxWeight().doubleValue() : null)
                .availableCapacity(request.getAvailableCapacity() != null ? request.getAvailableCapacity().doubleValue() : null)
                .acceptedCargoTypes(request.getAcceptedCargoTypes() != null ? request.getAcceptedCargoTypes() : List.of())
                .description(request.getDescription())
                .price(request.getPrice() != null ? request.getPrice().doubleValue() : null)
                .status(Trip.TripStatus.ACTIVE)
                .build();
        
        Trip savedTrip = tripRepository.save(trip);
        return mapToTripResponse(savedTrip);
    }
    
    public TripResponse getTripById(Long tripId, Long userId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        
        // Check if user is the driver or if trip is public
        if (!trip.getDriver().getId().equals(userId)) {
            throw new UnauthorizedException("Access denied");
        }
        
        return mapToTripResponse(trip);
    }
    
    public Page<TripResponse> getDriverTrips(Long driverId, Pageable pageable) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        
        Page<Trip> trips = tripRepository.findByDriverOrderByCreatedAtDesc(driver, pageable);
        return trips.map(this::mapToTripResponse);
    }
    
    public List<TripResponse> getDriverActiveTrips(Long driverId) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        
        List<Trip> trips = tripRepository.findByDriverAndStatusOrderByDepartureTimeAsc(driver, Trip.TripStatus.ACTIVE);
        return trips.stream().map(this::mapToTripResponse).collect(Collectors.toList());
    }
    
    public List<TripResponse> getDriverCompletedTrips(Long driverId) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        
        List<Trip> trips = tripRepository.findCompletedTripsByDriver(driver);
        return trips.stream().map(this::mapToTripResponse).collect(Collectors.toList());
    }
    
    public TripResponse updateTripStatus(Long tripId, Long driverId, Trip.TripStatus status) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        
        if (!trip.getDriver().getId().equals(driverId)) {
            throw new UnauthorizedException("Only the trip driver can update the status");
        }
        
        trip.setStatus(status);
        Trip updatedTrip = tripRepository.save(trip);
        return mapToTripResponse(updatedTrip);
    }
    
    public void deleteTrip(Long tripId, Long driverId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        
        if (!trip.getDriver().getId().equals(driverId)) {
            throw new UnauthorizedException("Only the trip driver can delete the trip");
        }
        
        // Check if there are any accepted requests
        long acceptedRequests = shipmentRequestRepository.countByTripAndStatus(trip, ShipmentRequest.RequestStatus.ACCEPTED);
        if (acceptedRequests > 0) {
            throw new IllegalStateException("Cannot delete trip with accepted shipment requests");
        }
        
        tripRepository.delete(trip);
    }
    
    public Page<TripResponse> searchTrips(TripSearchRequest searchRequest, Pageable pageable) {
        Page<Trip> trips = tripRepository.findAvailableTripsWithCriteria(
            searchRequest.getDepartureLocation(),
            searchRequest.getDestinationLocation(),
            searchRequest.getDepartureDate(),
            searchRequest.getCargoTypes(),
            searchRequest.getMaxWeight(),
            searchRequest.getMaxLength(),
            searchRequest.getMaxWidth(),
            searchRequest.getMaxHeight(),
            pageable
        );
        return trips.map(this::mapToTripResponse);
    }

    public TripResponse getTripDetails(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
        return mapToTripResponse(trip);
    }
    
    private TripResponse mapToTripResponse(Trip trip) {
        long pendingRequests = shipmentRequestRepository.countByTripAndStatus(trip, ShipmentRequest.RequestStatus.PENDING);
        long acceptedRequests = shipmentRequestRepository.countByTripAndStatus(trip, ShipmentRequest.RequestStatus.ACCEPTED);
        
        return TripResponse.builder()
                .id(trip.getId())
                .driverId(trip.getDriver().getId())
                .driverName(trip.getDriver().getFirstName() + " " + trip.getDriver().getLastName())
                .departureLocation(trip.getDepartureLocation())
                .destinationLocation(trip.getDestinationLocation())
                .intermediateStops(trip.getIntermediateStops())
                .departureTime(trip.getDepartureTime())
                .estimatedArrivalTime(trip.getEstimatedArrivalTime())
                .maxLength(trip.getMaxLength())
                .maxWidth(trip.getMaxWidth())
                .maxHeight(trip.getMaxHeight())
                .maxWeight(trip.getMaxWeight())
                .availableCapacity(trip.getAvailableCapacity())
                .acceptedCargoTypes(trip.getAcceptedCargoTypes())
                .description(trip.getDescription())
                .status(trip.getStatus())
                .price(trip.getPrice())
                .createdAt(trip.getCreatedAt())
                .updatedAt(trip.getUpdatedAt())
                .pendingRequestsCount((int) pendingRequests)
                .acceptedRequestsCount((int) acceptedRequests)
                .build();
    }
} 