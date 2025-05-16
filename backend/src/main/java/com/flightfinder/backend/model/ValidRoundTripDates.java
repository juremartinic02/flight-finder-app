package com.flightfinder.backend.model;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidRoundTripDatesValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface ValidRoundTripDates {

    String message() default "Return date must be on or after the departure date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
