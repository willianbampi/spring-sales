package com.next.ecommerce.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.next.ecommerce.validation.constraint.NotEmptyListValidation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NotEmptyListValidation.class)
public @interface NotEmptyList {

    String message() default "The list cannot be empty or null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
}
