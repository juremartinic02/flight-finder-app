package com.flightfinder.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat; // Formats LocalDate as yyyy-MM-dd in JSON
import jakarta.validation.constraints.*;           // Bean Validation annotations for input checks
import lombok.*;                                   // Lombok to generate boilerplate (getters, etc.)

import java.time.LocalDate;

/**
 * DTO for capturing flight search parameters from the client.
 */

@ValidRoundTripDates //Ensures that departureDate >= returnDate
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchRequest {

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$", // ^ = start of the input ; [A-Z]{3} = three uppercase letters ; & = end of the input so that for example EUR123 isn't valid
             message = "Origin must be a valid 3-letter IATA code")
    private String origin;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}$",
             message = "Destination must be a valid 3-letter IATA code")
    private String destination;

    @NotNull(message = "Departure date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;
    
    // there is no need to have @NotNull or @NotBlank on returnDate because person could buy a one-way ticket
    @JsonFormat(pattern = "yyyy-MM-dd") 
    private LocalDate returnDate;

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
