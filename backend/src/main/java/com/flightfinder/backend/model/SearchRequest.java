package com.flightfinder.backend.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO(Data Transfer Object) for capturing AND VALIDATING flight search parameters recieved from the clients.
 * SearchRequest class has a custom class-level validation to ensure date consistency which means that
 * it ensures that departureDate >= returnDate
 */

@ValidRoundTripDates //Ensures that departureDate >= returnDate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$", // ^ = start of the input ; [A-Z]{3} = three uppercase letters ; $ = end of the input so that for example EUR123 isn't valid
             message = "Origin must be a valid 3-letter IATA code")
    private String origin;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$",
             message = "Destination must be a valid 3-letter IATA code")
    private String destination;

    @NotNull(message = "Departure date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;

    // The returnDate doesnt have to be checked by @NotNull or @NotBlank because person could buy a one-way ticket
    @JsonFormat(pattern = "yyyy-MM-dd") 
    private LocalDate returnDate;

    /**
     * Assuring that children or/and infants cant travel alone min adult is 1,
     * even if there are no children or infants minimum adult travelers should be 1
     */
    @NotNull(message = "Number of adults is required")
    @Min(value = 1, message = "At least one adult is required")
    private Integer adults;

    @NotNull
    @Min(value = 0, message = "Children count can't be negative")
    @Builder.Default
    private Integer children = 0;

    @NotNull
    @Min(value = 0, message = "Infants count can't be negative")
    @Builder.Default
    private Integer infants = 0;

    @NotBlank
    @Pattern(regexp = "^(USD|EUR|HRK)$",
             message = "Currency must be USD, EUR, or HRK")
    private String currency;
}
