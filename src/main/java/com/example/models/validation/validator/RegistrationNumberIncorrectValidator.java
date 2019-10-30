package com.example.models.validation.validator;

import com.example.models.validation.annotation.IncorrectRegistrationNumber;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationNumberIncorrectValidator implements ConstraintValidator<IncorrectRegistrationNumber, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if (s == null || s.length() > 7) {
            return false;
        }

        Pattern pattern = Pattern.compile("[A-Z]{2}\\d{5}");
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }
}
