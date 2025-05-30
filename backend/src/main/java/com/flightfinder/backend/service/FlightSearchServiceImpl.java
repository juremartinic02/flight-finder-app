package com.flightfinder.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.flightfinder.backend.exception.AmadeusApiErrorException;
import com.flightfinder.backend.exception.InvalidSearchRequestException;
import com.flightfinder.backend.external.AmadeusClient;
import com.flightfinder.backend.model.FlightOffer;
import com.flightfinder.backend.model.SearchRequest;
import com.flightfinder.backend.model.external.FlightOffersSearchResponse;
import com.flightfinder.backend.repository.FlightOfferRepository;

import lombok.RequiredArgsConstructor;

/**
 * FlightSearchServiceImpl service class is responsible for the flight search process including:
 * - Checking for cached results in the local database
 * - Calling the Amadeus API if no suitable cache exists (AmadeusClient.java)
 * - Mapps the Amadeus API response
 * - Saves newly fetched offers to the cache
 */
@Service // Makes class eligible for DI (dependency injection)
@RequiredArgsConstructor
public class FlightSearchServiceImpl implements FlightSearchService {

    private static final Logger logger = LoggerFactory.getLogger(FlightSearchServiceImpl.class);

    // Repository for accessing cached flight offers from the database
    private final FlightOfferRepository repo;

    // Client for communicating with the Amadeus API
    private final AmadeusClient amadeusClient;

    /**
     * Searches for flight offers based on the provided search request from the user.
     * It first attempts to retrieve matching offers from the local cache.
     * If no matched offers are found in the cache, it fetches them from the Amadeus API,
     * maps the response to FlightOffer.java entities, saves them to the cache, and then returns them to the user.
     */
    @Override
    public List<FlightOffer> searchFlights(SearchRequest req) {
        LocalDateTime departureDateStart = req.getDepartureDate().atStartOfDay(); // Start of the departure day
        LocalDateTime departureDateEnd = req.getDepartureDate().plusDays(1).atStartOfDay(); // Start of the day after departure day

        List<FlightOffer> cachedOffers;
        boolean isReturnTrip = req.getReturnDate() != null;
        
        // --- CHECK CACHE ---
        if (isReturnTrip) {
            LocalDateTime returnDateStart = req.getReturnDate().atStartOfDay();
            LocalDateTime returnDateEnd = req.getReturnDate().plusDays(1).atStartOfDay();

            logger.info("Searching cache for ROUND TRIP flights: {}->{}, Departure range: [{} to {}), Return range: [{} to {}), Pax: A-{},C-{},I-{}, Curr:{}",
                    req.getOrigin(), req.getDestination(),
                    departureDateStart, departureDateEnd,
                    returnDateStart, returnDateEnd,
                    req.getAdults(), req.getChildren(), req.getInfants(), req.getCurrency());

            cachedOffers = repo.findFlightOffersBySearchCriteria(
                    req.getOrigin(), req.getDestination(),
                    req.getAdults(), req.getChildren(), req.getInfants(), req.getCurrency(),
                    departureDateStart, departureDateEnd,
                    true, // isReturnTrip
                    returnDateStart, returnDateEnd
            );
        } else { // One-way trip
            logger.info("Searching cache for ONE-WAY flights: {}->{}, Departure range: [{} to {}), Pax: A-{},C-{},I-{}, Curr:{}",
                    req.getOrigin(), req.getDestination(),
                    departureDateStart, departureDateEnd,
                    req.getAdults(), req.getChildren(), req.getInfants(), req.getCurrency());

            cachedOffers = repo.findFlightOffersBySearchCriteria(
                    req.getOrigin(), req.getDestination(),
                    req.getAdults(), req.getChildren(), req.getInfants(), req.getCurrency(),
                    departureDateStart, departureDateEnd,
                    false,
                    null,
                    null
            );
        }

        if (!cachedOffers.isEmpty()) {
            logger.info("Found {} offers in cache.", cachedOffers.size());
            return cachedOffers; // Return cached results if found
        }

        // --- IF CACHE MISS, CALL AMADEUS API ---
        logger.info("No suitable offers found in cache. Calling Amadeus API.");
        FlightOffersSearchResponse resp;
        try {
            // Call AmadeusClient to get flight offers
            resp = amadeusClient.searchOffers(req).block();
        } catch (AmadeusApiErrorException | InvalidSearchRequestException e) {
            logger.error("Amadeus API call failed: {}", e.getMessage());
            throw e; // Re-throw to be handled by GlobalExceptionHandler
        } catch (Exception e) { // Catch any other unexpected exceptions during the API call
            logger.error("Unexpected error calling AmadeusClient: {}", e.getMessage(), e);
            throw new AmadeusApiErrorException("Failed to fetch flight offers due to an unexpected error: " + e.getMessage(), e);
        }

        // Check if the Amadeus API returned any data
        if (resp == null || resp.getData() == null || resp.getData().isEmpty()) {
            logger.info("Amadeus API returned no offers or empty data.");
            return Collections.emptyList();
        }
        logger.info("Received {} offers from Amadeus API. Mapping and saving to cache.", resp.getData().size());

        // --- MAP API RESPONSE AND SAVE TO CACHE ---
        List<FlightOffer> offers;
        try {
            // Map the Amadeus DTOs to FlightOffer entities
            offers = mapAndBuildFlightOffers(resp, req);
        } catch (Exception e) {
            logger.error("Critical error during mapping of Amadeus response: {}", e.getMessage(), e);
            throw new AmadeusApiErrorException("Error processing flight data from Amadeus: " + e.getMessage(), e);
        }

        // Save the newly fetched and mapped offers to the cache
        if (!offers.isEmpty()) {
            try {
                repo.saveAll(offers);
                logger.info("Successfully saved {} new offers to cache.", offers.size());
            } catch (Exception e) {
                logger.error("Error saving offers to cache: {}", e.getMessage(), e);
            }
        }
        return offers;
    }

    /**
     * Maps the raw FlightOffersSearchResponse from Amadeus to a list of FlightOffer entities.
     * This method handles the transformation of data structures and calculates fields like transfers.
     */
    private List<FlightOffer> mapAndBuildFlightOffers(FlightOffersSearchResponse resp, SearchRequest req) {
        // Stream through each individual offer data in the Amadeus response.
        return resp.getData().stream()
            .map(data -> {
                try {
                    FlightOffer.FlightOfferBuilder offerBuilder = FlightOffer.builder()
                            .origin(req.getOrigin())
                            .destination(req.getDestination())
                            .adults(req.getAdults())
                            .children(req.getChildren())
                            .infants(req.getInfants());

                    if (data.getPrice() != null) {
                        if (data.getPrice().getCurrency() != null) {
                            offerBuilder.currency(data.getPrice().getCurrency());
                        } else {
                            logger.warn("Offer missing currency. Itinerary data: {}", 
                                (data.getItineraries() != null && !data.getItineraries().isEmpty() ? data.getItineraries().get(0) : "N/A"));
                        }
                        if (data.getPrice().getTotal() != null) {
                            offerBuilder.totalPrice(new BigDecimal(data.getPrice().getTotal()));
                        } else {
                            logger.warn("Offer missing total price. Itinerary data: {}", 
                                (data.getItineraries() != null && !data.getItineraries().isEmpty() ? data.getItineraries().get(0) : "N/A"));
                            return null; // Skip this offer if total price is missing
                        }
                    } else {
                        logger.warn("Offer missing price block. Itinerary data: {}", 
                            (data.getItineraries() != null && !data.getItineraries().isEmpty() ? data.getItineraries().get(0) : "N/A"));
                        return null;
                    }

                    if (data.getItineraries() != null && !data.getItineraries().isEmpty()) {
                        var outboundItinerary = data.getItineraries().get(0);
                        if (outboundItinerary != null && outboundItinerary.getSegments() != null && !outboundItinerary.getSegments().isEmpty()) {
                            var outSeg = outboundItinerary.getSegments().get(0);
                            if (outSeg != null && outSeg.getDeparture() != null && outSeg.getDeparture().getAt() != null) {
                                offerBuilder.departureDateTime(LocalDateTime.parse(outSeg.getDeparture().getAt()));
                            } else {
                                 logger.warn("Offer missing critical outbound departure details. Itinerary: {}", outboundItinerary);
                                 return null;
                            }
                            offerBuilder.outboundTransfers(Math.max(0, outboundItinerary.getSegments().size() - 1));
                        } else {
                            logger.warn("Offer missing outbound segments. Itinerary: {}", outboundItinerary);
                            return null;
                        }

                        if (req.getReturnDate() != null) {
                            if (data.getItineraries().size() > 1) {
                                var inboundItinerary = data.getItineraries().get(1);
                                if (inboundItinerary != null && inboundItinerary.getSegments() != null && !inboundItinerary.getSegments().isEmpty()) {
                                    var inSeg = inboundItinerary.getSegments().get(0);
                                    if (inSeg != null && inSeg.getDeparture() != null && inSeg.getDeparture().getAt() != null) {
                                        offerBuilder.returnDateTime(LocalDateTime.parse(inSeg.getDeparture().getAt()));
                                        offerBuilder.inboundTransfers(Math.max(0, inboundItinerary.getSegments().size() - 1));
                                    } else {
                                        logger.warn("Offer missing critical inbound departure details for round trip. Inbound Itinerary: {}", inboundItinerary);
                                        offerBuilder.returnDateTime(null);
                                        offerBuilder.inboundTransfers(null);
                                    }
                                } else {
                                     logger.warn("Round trip search but second itinerary (inbound) segments missing or empty. Itineraries: {}", data.getItineraries());
                                     offerBuilder.returnDateTime(null); 
                                     offerBuilder.inboundTransfers(null);
                                }
                            } else {
                                 logger.warn("Round trip search but second itinerary (inbound) not provided by Amadeus. Itineraries: {}", data.getItineraries());
                                 offerBuilder.returnDateTime(null);
                                 offerBuilder.inboundTransfers(null);
                            }
                        } else { // One-way trip
                            offerBuilder.returnDateTime(null); // Ensure returnDateTime is null
                            offerBuilder.inboundTransfers(null); // Ensure inboundTransfers is null
                        }
                    } else {
                        logger.warn("Offer missing itineraries entirely. Price data: {}", data.getPrice());
                        return null;
                    }
                    // The 'createdAt' field will be set automatically by @PrePersist in FlightOffer entity when saving
                    return offerBuilder.build(); // Build the FlightOffer object

                } catch (NullPointerException | IndexOutOfBoundsException | DateTimeParseException | NumberFormatException e) {
                    logger.warn("Skipping one offer due to mapping error: {}. Amadeus 'data' object (potentially large, showing price part): {}", 
                                e.getMessage(), data.getPrice());
                    return null;
                }
            })
            .filter(Objects::nonNull) // Remove any offers that failed mapping
            .collect(Collectors.toList()); // Collect successfully mapped offers into a list
    }
}