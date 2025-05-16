package com.flightfinder.backend.repository;

import com.flightfinder.backend.model.FlightOffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FlightOfferRepositoryTest {

    @Autowired
    private FlightOfferRepository repo;

    @Test
    @DisplayName("findBy… returns matching offer for one-way trip")
    void testFindByOneWay() {
        // given
        FlightOffer offer = FlightOffer.builder()
            .origin("ZAG")
            .destination("LHR")
            .departureDateTime(LocalDateTime.of(2025,6,1,0,0))
            .returnDateTime(null)
            .transfers(1)
            .adults(1).children(0).infants(0)
            .currency("EUR")
            .totalPrice(BigDecimal.valueOf(200.00))
            .build();
        repo.save(offer);

        // when
        List<FlightOffer> results = repo
          .findByOriginAndDestinationAndDepartureDateTimeAndReturnDateTimeAndAdultsAndChildrenAndInfantsAndCurrency(
              "ZAG", "LHR",
              LocalDateTime.of(2025,6,1,0,0), null,
              1, 0, 0,
              "EUR"
          );

        // then
        assertThat(results).hasSize(1)
                           .first()
                           .usingRecursiveComparison()
                           .isEqualTo(offer);
    }

    @Test
    @DisplayName("findBy… returns empty when no match")
    void testFindByNoMatch() {
        List<FlightOffer> results = repo
          .findByOriginAndDestinationAndDepartureDateTimeAndReturnDateTimeAndAdultsAndChildrenAndInfantsAndCurrency(
              "ABC", "DEF",
              LocalDateTime.of(2025,1,1,0,0), null,
              1, 0, 0,
              "USD"
          );
        assertThat(results).isEmpty();
    }
}
