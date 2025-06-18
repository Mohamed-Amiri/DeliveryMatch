package com.deliverymatch.repository;

import com.deliverymatch.entity.Trip;
import com.deliverymatch.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    
    // Find trips by driver
    Page<Trip> findByDriverOrderByCreatedAtDesc(User driver, Pageable pageable);
    
    // Find active trips by driver
    List<Trip> findByDriverAndStatusOrderByDepartureTimeAsc(User driver, Trip.TripStatus status);
    
    // Find trips by status
    Page<Trip> findByStatusOrderByDepartureTimeAsc(Trip.TripStatus status, Pageable pageable);
    
    // Find trips with available capacity
    @Query("SELECT t FROM Trip t WHERE t.status = 'ACTIVE' AND t.availableCapacity > 0 AND t.departureTime > :now")
    Page<Trip> findAvailableTrips(@Param("now") LocalDateTime now, Pageable pageable);
    
    // Find trips by departure and destination locations
    @Query("SELECT t FROM Trip t WHERE t.status = 'ACTIVE' AND " +
           "LOWER(t.departureLocation) LIKE LOWER(CONCAT('%', :departure, '%')) AND " +
           "LOWER(t.destinationLocation) LIKE LOWER(CONCAT('%', :destination, '%')) AND " +
           "t.departureTime > :now")
    Page<Trip> findByDepartureAndDestination(@Param("departure") String departure, 
                                           @Param("destination") String destination,
                                           @Param("now") LocalDateTime now, 
                                           Pageable pageable);
    
    // Count trips by driver and status
    long countByDriverAndStatus(User driver, Trip.TripStatus status);
    
    // Find completed trips by driver
    @Query("SELECT t FROM Trip t WHERE t.driver = :driver AND t.status = 'COMPLETED' ORDER BY t.updatedAt DESC")
    List<Trip> findCompletedTripsByDriver(@Param("driver") User driver);
} 