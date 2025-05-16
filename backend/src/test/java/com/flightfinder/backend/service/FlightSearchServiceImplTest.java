package com.flightfinder.backend.service;

import com.flightfinder.backend.model.FlightOffer;
import com.flightfinder.backend.model.SearchRequest;
import com.flightfinder.backend.repository.FlightOfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class FlightSearchServiceImplTest {

    @Mock
    private FlightOfferRepository repo;

    @InjectMocks
    private FlightSearchServiceImpl service;

    private SearchRequest req;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        req = SearchRequest.builder()
            .origin("ZAG").destination("LHR")
            .departureDate(LocalDate.of(2025,6,1))
            .returnDate(LocalDate.of(2025,6,10))
            .adults(1).children(0).infants(0)
            .currency("EUR")
            .build();
    }

    @Test
    void returnsCachedResultsWhenPresent() {
        FlightOffer existing = FlightOffer.builder()
            .origin("ZAG").destination("LHR")
            .departureDateTime(req.getDepartureDate().atStartOfDay())
            .returnDateTime(req.getReturnDate().atStartOfDay())
            .transfers(0)
            .adults(1).children(0).infants(0)
            .currency("EUR")
            .totalPrice(BigDecimal.valueOf(150.00))
            .build();

        given(repo.findByOriginAndDestinationAndDepartureDateTimeAndReturnDateTimeAndAdultsAndChildrenAndInfantsAndCurrency(
                anyString(), anyString(),
                any(), any(),
                anyInt(), anyInt(), anyInt(),
                anyString()))
            .willReturn(List.of(existing));

        List<FlightOffer> result = service.searchFlights(req);

        assertThat(result).containsExactly(existing);
        then(repo).should(never()).save(any());
    }

    @Test
    void createsAndSavesNewOfferWhenCacheEmpty() {
        given(repo.findByOriginAndDestinationAndDepartureDateTimeAndReturnDateTimeAndAdultsAndChildrenAndInfantsAndCurrency(
                anyString(), anyString(),
                any(), any(),
                anyInt(), anyInt(), anyInt(),
                anyString()))
            .willReturn(List.of());

        List<FlightOffer> result = service.searchFlights(req);

        assertThat(result).hasSize(1);
        then(repo).should().save(any(FlightOffer.class));
        
        FlightOffer created = result.get(0);
        assertThat(created.getOrigin()).isEqualTo("ZAG");
        assertThat(created.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(123.45));
    }
}
