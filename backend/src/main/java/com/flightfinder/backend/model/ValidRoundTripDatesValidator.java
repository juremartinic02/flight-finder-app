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

    @Override
    public boolean isValid(SearchRequest req, ConstraintValidatorContext ctx) {
        // If the object itself is null, skip (other @NotNull will catch that)
        if (req == null) {
            return true;
        }

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
}
