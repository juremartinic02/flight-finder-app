package com.flightfinder.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Flight offer entity that is stored in the local database,
 * acting as a cache for results retrieved from the Amadeus API
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

    /**
     * Primary key for the FlightOffer entity.
     * Primary key will be automatically generated from the database itself.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * IATA code of the origin airport, cant be null, max 3 characters
     */
    @Column(nullable = false, length = 3)
    private String origin;

    /**
     * IATA code of the destination airport, cant be null, max 3 characters
     */
    @Column(nullable = false, length = 3)
    private String destination;

    /**
     * The date and time of departure for the flight offer
     */
    @Column(name = "departure_date", nullable = false)
    private LocalDateTime departureDateTime;

    /**
     * The date and time of departure for the return flight of the flight offer
     */
    @Column(name = "return_date")
    private LocalDateTime returnDateTime;

    /**
     * Number of stops in the outbound part of the journey plan
     */
    @Column(name = "outbound_transfers", nullable = false)
    private Integer outboundTransfers;

    /**
     * Number of stops in the inbound part of the journey plan
     */
    @Column(name = "inbound_transfers")
    private Integer inboundTransfers;

    @Column(nullable = false)
    private Integer adults;

    @Column(nullable = false)
    private Integer children;

    @Column(nullable = false)
    private Integer infants;

    @Column(nullable = false, length = 3)
    private String currency;

    /**
     * Total price for this flight offer for all passengers
     */
    @Column(name = "total_price", nullable = false, precision = 13, scale = 2)
    private BigDecimal totalPrice;

    /**
     * Timestamp indicating when this flight offer record was first created and saved to the cache
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Before a NEW FlighOffer object is saved to the database for the first time, the
     * on create method is automatically called because it has annotation @PrePersist and the method sets onCreate time
     * to now.
     */
    @PrePersist
    private void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}