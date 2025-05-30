package com.flightfinder.backend.model;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Custom validation annotation to ensure that the return date is not before the departure date.
 * The actual validation logic is implemented in ValidRoundTripDatesValidation.java
 */
@Documented // ensures @ValidRoundTripDates appears in Javadoc for any class it's applied to
@Constraint(validatedBy = ValidRoundTripDatesValidator.class)
@Target(TYPE) // ensures that @ValidRoundTripDates can only appear at the top of a class, not on individual fields
@Retention(RUNTIME)
public @interface ValidRoundTripDates {

    String message() default "Return date must be on or after the departure date"; // Default error message

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
