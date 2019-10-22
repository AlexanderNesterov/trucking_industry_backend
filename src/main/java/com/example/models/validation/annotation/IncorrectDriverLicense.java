package com.example.models.validation.annotation;

import com.example.models.validation.validator.DriverLicenseIncorrectValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = DriverLicenseIncorrectValidator.class)
public @interface IncorrectDriverLicense {
    String message() default "incorrect driver license";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
