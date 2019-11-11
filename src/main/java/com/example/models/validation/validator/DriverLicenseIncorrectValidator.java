package com.example.models.validation.validator;

import com.example.models.validation.annotation.DriverLicense;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DriverLicenseIncorrectValidator implements ConstraintValidator<DriverLicense, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.length() == 0) {
            return false;
        }

        Pattern pattern = Pattern.compile("\\d{10}");
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }
}
