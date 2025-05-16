package com.flightfinder.backend.controller;

import com.flightfinder.backend.model.FlightOffer;
import com.flightfinder.backend.model.SearchRequest;
import com.flightfinder.backend.service.FlightSearchService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for flight search operations.
 */
@RestController
@RequestMapping("/api")   // you can change this prefix if you prefer
public class FlightSearchController {

    private final FlightSearchService flightSearchService;

    public FlightSearchController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    /**
     * POST /api/search
     * 
     * Consumes a SearchRequest JSON, validates it, 
     * and returns matching FlightOffer objects.
     */
    @PostMapping("/search")
    public List<FlightOffer> searchFlights(@Valid @RequestBody SearchRequest request) {
        return flightSearchService.searchFlights(request);
    }
}
