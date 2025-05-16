package com.flightfinder.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a flight search result cached locally.
 */
@Entity
@Table(
    name = "flight_offers",
    indexes = {
        @Index(
            name = "idx_search_params",
            columnList = "origin, destination, departure_date, return_date, adults, children, infants, currency"
        )
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightOffer {

    /** Primary key */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** IATA code of departure airport */
    @Column(nullable = false, length = 3)
    private String origin;

    /** IATA code of arrival airport */
    @Column(nullable = false, length = 3)
    private String destination;

    /** Departure timestamp; if you need just date, switch to LocalDate */
    @Column(name = "departure_date", nullable = false)
    private LocalDateTime departureDateTime;

    /** Return timestamp; null for one-way */
    @Column(name = "return_date")
    private LocalDateTime returnDateTime;

    /** Number of stops/transfers in the itinerary */
    @Column(nullable = false)
    private Integer transfers;

    /** Passenger counts matching the search request */
    @Column(nullable = false)
    private Integer adults;
    @Column(nullable = false)
    private Integer children;
    @Column(nullable = false)
    private Integer infants;

    /** Currency code for prices */
    @Column(nullable = false, length = 3)
    private String currency;

    /** Total price for this offer */
    @Column(name = "total_price", nullable = false, precision = 13, scale = 2)
    private BigDecimal totalPrice;

    /** When this offer was first cached */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

