package com.flightfinder.backend.external;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.flightfinder.backend.exception.AmadeusApiErrorException;
import com.flightfinder.backend.exception.InvalidSearchRequestException;
import com.flightfinder.backend.model.SearchRequest;
import com.flightfinder.backend.model.external.FlightOffersSearchResponse;

import reactor.core.publisher.Mono;

/**
 * Client component that interacts with Amadeus API to fetch flight offer information
 */
@Component
public class AmadeusClient {
    private static final Logger logger = LoggerFactory.getLogger(AmadeusClient.class);
    private final WebClient client;

    // Inject the configured maximum number of flight results to fetch from application.properties
    @Value("${amadeus.api.max-results:10}") // Default to 10 if property not found
    private int maxResults;

    public AmadeusClient(
        @Qualifier("amadeusWebClient") WebClient amadeusWebClient
    ) {
        this.client = amadeusWebClient;
    }

    /**
     * Searches for the flight offers on the Amadeus API based on the search criteria that user inputs
     */
    public Mono<FlightOffersSearchResponse> searchOffers(SearchRequest req) {
        return client.get() // HTTP GET request
            .uri(uriBuilder -> uriBuilder
                .path("/v2/shopping/flight-offers") // Specific Amadeus endpoint for flight offers
                .queryParam("originLocationCode", req.getOrigin())
                .queryParam("destinationLocationCode", req.getDestination())
                .queryParam("departureDate", req.getDepartureDate().toString()) // Dates are converted to "YYYY-MM-DD" string
                .queryParamIfPresent("returnDate", // Add returnDate only if it's present in the request (for one-way vs. round-trip)
                    req.getReturnDate() != null
                        ? Optional.of(req.getReturnDate().toString()) // Ensure date is string
                        : Optional.empty()
                )
                .queryParam("adults", req.getAdults())
                .queryParam("children", req.getChildren())
                .queryParam("infants", req.getInfants())
                .queryParam("currencyCode", req.getCurrency())
                .queryParam("max", this.maxResults) // Use the injected value from application.properties
                .build()
            )
            .retrieve() // Initiate the req. and retrieve the response
            // Error handling for specific HTTP status codes from Amadeus
            .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(), clientResponse -> {
                logger.error("Amadeus API Client Error: Status {}, Body: {}", clientResponse.statusCode(), clientResponse.bodyToMono(String.class).defaultIfEmpty("<empty body>"));
                if (clientResponse.statusCode().equals(HttpStatus.BAD_REQUEST)) {
                    return clientResponse.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new InvalidSearchRequestException("Amadeus rejected search parameters: " + body)));
                }
                return clientResponse.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new AmadeusApiErrorException("Client error calling Amadeus: " + clientResponse.statusCode() + " - " + body)));
            })
            .onStatus(httpStatusCode -> httpStatusCode.is5xxServerError(), clientResponse -> {
                logger.error("Amadeus API Server Error: Status {}, Body: {}", clientResponse.statusCode(), clientResponse.bodyToMono(String.class).defaultIfEmpty("<empty body>"));
                return clientResponse.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new AmadeusApiErrorException("Server error from Amadeus: " + clientResponse.statusCode() + " - " + body)));
            })
            .bodyToMono(FlightOffersSearchResponse.class)
            .doOnError(error -> logger.error("Error during Amadeus API call or response processing: {}", error.getMessage(), error))
            .onErrorResume(e -> {
                if (e instanceof AmadeusApiErrorException || e instanceof InvalidSearchRequestException) {
                    return Mono.error(e);
                }
                logger.error("Unexpected error in AmadeusClient: ", e);
                return Mono.error(new AmadeusApiErrorException("Unexpected error communicating with Amadeus: " + e.getMessage(), e));
            });
    }
}