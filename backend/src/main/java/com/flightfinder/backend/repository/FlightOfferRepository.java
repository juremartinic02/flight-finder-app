package com.flightfinder.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.flightfinder.backend.model.FlightOffer;

/**
 * FlightOfferRepository interface handles all database operations for flight offers.
 */
public interface FlightOfferRepository extends JpaRepository<FlightOffer, Long> {

    /**
     * Finds cached offers matching the search criteria.
     * This query supports the caching mechanism by looking for flights that match the users request parameters.
     * Departure and return dates are matched for any time within the given day.
     */
    @Query("SELECT fo FROM FlightOffer fo WHERE " +
           "fo.origin = :origin AND " +
           "fo.destination = :destination AND " +
           "fo.adults = :adults AND " +
           "fo.children = :children AND " +
           "fo.infants = :infants AND " +
           "fo.currency = :currency AND " +
           "fo.departureDateTime >= :departureDateStart AND fo.departureDateTime < :departureDateEnd AND " +
           "((:isReturnTrip = false AND fo.returnDateTime IS NULL) OR " + // Handles one-way
           " (:isReturnTrip = true AND fo.returnDateTime >= :returnDateStart AND fo.returnDateTime < :returnDateEnd))") // Handles round-trip
    List<FlightOffer> findFlightOffersBySearchCriteria(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("adults") Integer adults,
            @Param("children") Integer children,
            @Param("infants") Integer infants,
            @Param("currency") String currency,
            @Param("departureDateStart") LocalDateTime departureDateStart,
            @Param("departureDateEnd") LocalDateTime departureDateEnd,
            @Param("isReturnTrip") boolean isReturnTrip,
            @Param("returnDateStart") LocalDateTime returnDateStart, // Can be null if isReturnTrip is false
            @Param("returnDateEnd") LocalDateTime returnDateEnd     // Can be null if isReturnTrip is false
    );
}