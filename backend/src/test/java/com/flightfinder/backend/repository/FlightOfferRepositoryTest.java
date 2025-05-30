// IMPORTANT: FlightOfferRepositoryTest.java was created by AI

package com.flightfinder.backend.repository;

import com.flightfinder.backend.model.FlightOffer; // The entity being tested
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest; // For testing JPA repositories
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager; // For persisting test data

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat; // For fluent assertions

/**
 * Integration tests for the {@link FlightOfferRepository}.
 * Uses {@link DataJpaTest} to configure an in-memory H2 database and focus
 * only on JPA components. {@link TestEntityManager} can be used for more control
 * over persistence if needed, but direct repository usage is often sufficient.
 */
@DataJpaTest // Configures H2, Hibernate, Spring Data, etc., for JPA tests.
class FlightOfferRepositoryTest {

    @Autowired // Spring injects the repository instance.
    private FlightOfferRepository flightOfferRepository;

    // TestEntityManager can be used if you need to flush changes or use EntityManager specific methods.
    // For these tests, directly using the repository should be fine.
    // @Autowired
    // private TestEntityManager entityManager;

    private FlightOffer oneWayOffer;
    private FlightOffer roundTripOffer;

    @BeforeEach
    void setUp() {
        // Clean up before each test to ensure a fresh state.
        flightOfferRepository.deleteAll();

        // Common passenger and currency details for test offers
        Integer adults = 1;
        Integer children = 0;
        Integer infants = 0;
        String currency = "EUR";

        // Setup a one-way offer
        LocalDateTime departureOneWay = LocalDateTime.of(2025, 7, 10, 10, 0);
        oneWayOffer = FlightOffer.builder()
                .origin("LHR")
                .destination("CDG")
                .departureDateTime(departureOneWay)
                .returnDateTime(null) // Explicitly null for one-way
                .outboundTransfers(0) // Direct flight
                .inboundTransfers(null) // No inbound for one-way
                .adults(adults)
                .children(children)
                .infants(infants)
                .currency(currency)
                .totalPrice(new BigDecimal("120.50"))
                // createdAt will be set by @PrePersist
                .build();

        // Setup a round-trip offer
        LocalDateTime departureRoundTrip = LocalDateTime.of(2025, 8, 15, 14, 30);
        LocalDateTime returnRoundTrip = LocalDateTime.of(2025, 8, 22, 18, 0);
        roundTripOffer = FlightOffer.builder()
                .origin("JFK")
                .destination("AMS")
                .departureDateTime(departureRoundTrip)
                .returnDateTime(returnRoundTrip)
                .outboundTransfers(1) // 1 stop outbound
                .inboundTransfers(0)  // Direct return
                .adults(adults + 1) // Different passenger count for variety
                .children(children)
                .infants(infants)
                .currency("USD") // Different currency
                .totalPrice(new BigDecimal("750.00"))
                .build();

        // Save the offers to the test database
        flightOfferRepository.saveAll(List.of(oneWayOffer, roundTripOffer));
        // entityManager.flush(); // If using TestEntityManager and need to force persistence before query
    }

    @Test
    @DisplayName("findFlightOffersBySearchCriteria returns matching one-way offer")
    void testFindFlightOffersBySearchCriteria_OneWay_ReturnsMatch() {
        // ARRANGE
        // Parameters for the query that should match the saved oneWayOffer
        String origin = "LHR";
        String destination = "CDG";
        Integer adults = 1;
        Integer children = 0;
        Integer infants = 0;
        String currency = "EUR";
        // Date range for the departure day of oneWayOffer
        LocalDateTime departureDateStart = oneWayOffer.getDepartureDateTime().toLocalDate().atStartOfDay();
        LocalDateTime departureDateEnd = oneWayOffer.getDepartureDateTime().toLocalDate().plusDays(1).atStartOfDay();
        boolean isReturnTrip = false; // Searching for a one-way trip

        // ACT
        // Call the repository method with matching criteria
        List<FlightOffer> results = flightOfferRepository.findFlightOffersBySearchCriteria(
                origin, destination,
                adults, children, infants, currency,
                departureDateStart, departureDateEnd,
                isReturnTrip,
                null, null // returnDateStart and returnDateEnd are null for one-way
        );

        // ASSERT
        // Expect one result, and it should be the oneWayOffer we saved
        assertThat(results).hasSize(1);
        // Using recursive comparison to check all fields of the found offer against the saved one.
        // We need to ignore 'id' and 'createdAt' if they are auto-generated and might differ slightly
        // if we didn't flush and re-fetch the original 'oneWayOffer' after saving.
        // However, since we saved it and are comparing to the in-memory 'oneWayOffer' which now has an ID and createdAt,
        // a direct recursive comparison should work if @PrePersist sets createdAt correctly before comparison.
        // For robustness, especially if not re-fetching the saved entity, you might exclude auto-generated fields.
        FlightOffer foundOffer = results.get(0);
        assertThat(foundOffer.getId()).isEqualTo(oneWayOffer.getId()); // Check ID explicitly
        assertThat(foundOffer.getOrigin()).isEqualTo(oneWayOffer.getOrigin());
        assertThat(foundOffer.getDestination()).isEqualTo(oneWayOffer.getDestination());
        assertThat(foundOffer.getDepartureDateTime()).isEqualTo(oneWayOffer.getDepartureDateTime());
        assertThat(foundOffer.getReturnDateTime()).isNull();
        assertThat(foundOffer.getOutboundTransfers()).isEqualTo(oneWayOffer.getOutboundTransfers());
        assertThat(foundOffer.getInboundTransfers()).isNull();
        assertThat(foundOffer.getTotalPrice()).isEqualByComparingTo(oneWayOffer.getTotalPrice());
        // Or using recursive comparison:
        // assertThat(results.get(0)).usingRecursiveComparison().isEqualTo(oneWayOffer);
    }

    @Test
    @DisplayName("findFlightOffersBySearchCriteria returns matching round-trip offer")
    void testFindFlightOffersBySearchCriteria_RoundTrip_ReturnsMatch() {
        // --- ARRANGE ---
        String origin = "JFK";
        String destination = "AMS";
        Integer adults = 2; // Matches roundTripOffer
        Integer children = 0;
        Integer infants = 0;
        String currency = "USD";
        LocalDateTime departureDateStart = roundTripOffer.getDepartureDateTime().toLocalDate().atStartOfDay();
        LocalDateTime departureDateEnd = roundTripOffer.getDepartureDateTime().toLocalDate().plusDays(1).atStartOfDay();
        LocalDateTime returnDateStart = roundTripOffer.getReturnDateTime().toLocalDate().atStartOfDay();
        LocalDateTime returnDateEnd = roundTripOffer.getReturnDateTime().toLocalDate().plusDays(1).atStartOfDay();
        boolean isReturnTrip = true;

        // ACT
        List<FlightOffer> results = flightOfferRepository.findFlightOffersBySearchCriteria(
                origin, destination,
                adults, children, infants, currency,
                departureDateStart, departureDateEnd,
                isReturnTrip,
                returnDateStart, returnDateEnd
        );

        // ASSERT
        assertThat(results).hasSize(1);
        // Using recursive comparison is convenient here.
        assertThat(results.get(0)).usingRecursiveComparison().isEqualTo(roundTripOffer);
    }

    @Test
    @DisplayName("findFlightOffersBySearchCriteria returns empty list when no match found")
    void testFindFlightOffersBySearchCriteria_NoMatch() {
        // ARRANGE
        // Search parameters that do NOT match any saved offers
        String origin = "XXX"; // Non-existent origin
        String destination = "YYY";
        Integer adults = 1;
        Integer children = 0;
        Integer infants = 0;
        String currency = "GBP";
        LocalDateTime departureDateStart = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime departureDateEnd = LocalDateTime.of(2026, 1, 2, 0, 0);
        boolean isReturnTrip = false;

        // ACT
        List<FlightOffer> results = flightOfferRepository.findFlightOffersBySearchCriteria(
                origin, destination,
                adults, children, infants, currency,
                departureDateStart, departureDateEnd,
                isReturnTrip,
                null, null
        );

        // ASSERT
        // Expect an empty list as no offers should match these criteria
        assertThat(results).isEmpty();
    }
}