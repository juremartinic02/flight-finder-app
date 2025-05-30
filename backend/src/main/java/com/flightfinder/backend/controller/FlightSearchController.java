package com.flightfinder.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightfinder.backend.model.FlightOffer;
import com.flightfinder.backend.model.SearchRequest;
import com.flightfinder.backend.service.FlightSearchService;

import jakarta.validation.Valid;

/**
 * REST controller for flight search operations.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class FlightSearchController {

    private final FlightSearchService flightSearchService;

    public FlightSearchController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    /**
     * POST /api/search
     * 
     * Consumes a SearchRequest JSON, validates it and returns matching FlightOffer objects.
     */
    @PostMapping("/search")
    public List<FlightOffer> searchFlights(@Valid @RequestBody SearchRequest request) {
        return flightSearchService.searchFlights(request);
    }
}
