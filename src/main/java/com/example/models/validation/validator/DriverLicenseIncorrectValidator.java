package com.example.models.validation.validator;

import com.example.models.validation.annotation.IncorrectDriverLicense;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DriverLicenseIncorrectValidator implements ConstraintValidator<IncorrectDriverLicense, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Pattern pattern = Pattern.compile("\\d{10}");
        Matcher matcher = pattern.matcher(s);

        return matcher.find();
    }
}
