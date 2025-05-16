package com.flightfinder.backend.repository;

import com.flightfinder.backend.model.FlightOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for persisting and querying FlightOffer entities.
 */
public interface FlightOfferRepository
        extends JpaRepository<FlightOffer, Long> {

    /**
     * Find cached offers that exactly match the search parameters.
     *  
     * For a one-way trip, pass returnDateTime = null and JPA will generate
     * "return_date IS NULL" in SQL.
     */
    List<FlightOffer> findByOriginAndDestinationAndDepartureDateTimeAndReturnDateTimeAndAdultsAndChildrenAndInfantsAndCurrency(
        String origin,
        String destination,
        LocalDateTime departureDateTime,
        LocalDateTime returnDateTime,
        Integer adults,
        Integer children,
        Integer infants,
        String currency
    );
}
