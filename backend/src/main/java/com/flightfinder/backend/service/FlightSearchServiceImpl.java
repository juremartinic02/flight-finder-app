package com.flightfinder.backend.service;

import com.flightfinder.backend.model.FlightOffer;
import com.flightfinder.backend.model.SearchRequest;
import com.flightfinder.backend.repository.FlightOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Default implementation: 1) try cache, 2) otherwise create & save a dummy offer.
 */
@Service
@RequiredArgsConstructor  // Lombok: generates constructor for final fields
public class FlightSearchServiceImpl implements FlightSearchService {

    private final FlightOfferRepository repo;

    @Override
    public List<FlightOffer> searchFlights(SearchRequest req) {
        // 1) Try cached results
        List<FlightOffer> cached = repo.findByOriginAndDestinationAndDepartureDateTimeAndReturnDateTimeAndAdultsAndChildrenAndInfantsAndCurrency(
            req.getOrigin(),
            req.getDestination(),
            req.getDepartureDate().atStartOfDay(),
            req.getReturnDate() != null ? req.getReturnDate().atStartOfDay() : null,
            req.getAdults(),
            req.getChildren(),
            req.getInfants(),
            req.getCurrency()
        );
        if (!cached.isEmpty()) {
            return cached;
        }

        // 2) No cache hit → build a dummy offer, save, and return it
        FlightOffer offer = FlightOffer.builder()
            .origin(req.getOrigin())
            .destination(req.getDestination())
            .departureDateTime(req.getDepartureDate().atStartOfDay())
            .returnDateTime(req.getReturnDate() != null
                ? req.getReturnDate().atStartOfDay() : null)
            .transfers(1)
            .adults(req.getAdults())
            .children(req.getChildren())
            .infants(req.getInfants())
            .currency(req.getCurrency())
            .totalPrice(BigDecimal.valueOf(123.45))
            .build();

        repo.save(offer);
        return List.of(offer);
    }
}