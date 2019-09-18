package com.faziz.exercise.tradeledger.controller.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = FilterValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterValidationType {

    String message() default "Invalid filter!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
