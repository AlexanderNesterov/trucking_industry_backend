package com.example.models.validation.annotation;


import com.example.models.validation.validator.TextFieldValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = TextFieldValidator.class)
public @interface TextField {
    String message() default "incorrect text field";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}