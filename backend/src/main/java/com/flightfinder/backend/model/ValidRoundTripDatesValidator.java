package com.flightfinder.backend.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;


/**
 * Validator for the @ValidRoundTripDates annotation.
 * Ensures returnDate is not before departureDate.
 */
public class ValidRoundTripDatesValidator
        implements ConstraintValidator<ValidRoundTripDates, SearchRequest> {

    /**
     * isValid => main validation method that checks if the returnDate >= departureDate
     */
    @Override
    public boolean isValid(SearchRequest req, ConstraintValidatorContext ctx) {
        // If the object itself is null, skip (other @NotNull annotations will catch that)
        if (req == null) {
            return true;
        }

        // Get the departure and return dates from the SearchRequest object
        LocalDate dep = req.getDepartureDate();
        LocalDate ret = req.getReturnDate();

        // One-way trips are fine
        if (ret == null) {
            return true;
        }

        // If departureDate is missing or invalid, let field-level @NotNull handle it
        if (dep == null) {
            return true;
        }

        // If return is before departure → violation
        if (ret.isBefore(dep)) {
            // Attach the violation to the returnDate field
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate())
               .addPropertyNode("returnDate")
               .addConstraintViolation();
            return false;
        }

        return true;
    }

    /**
     * Allows validator to get any specific configurations from the annotation its linked to before it start
     * its actual validation with the isValid method
     */
    @Override
    public void initialize(ValidRoundTripDates constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
