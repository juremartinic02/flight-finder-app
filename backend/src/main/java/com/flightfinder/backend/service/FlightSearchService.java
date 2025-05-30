package com.flightfinder.backend.service;

import com.flightfinder.backend.model.FlightOffer;
import com.flightfinder.backend.model.SearchRequest;

import java.util.List;

/**
 * Defines the contract for searching flights.
 */
public interface FlightSearchService {
    /**
     * Returns cached FlightOffer results matching the given SearchRequest,
     * or fetches (and caches) new ones if none are present.
     */
    List<FlightOffer> searchFlights(SearchRequest request);
}
