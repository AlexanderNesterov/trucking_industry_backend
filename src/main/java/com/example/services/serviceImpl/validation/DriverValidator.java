package com.example.services.serviceImpl.validation;

import com.example.services.models.DriverDto;
import com.example.services.models.UserDto;
import com.example.services.serviceImpl.validation.exception.UserValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DriverValidator {
    private static DriverDto checkingDriver;

    public static void validate(DriverDto driver, boolean isUpdate) {
        checkingDriver = driver;

        checkDriverLicense();
        checkUser(isUpdate);

        checkingDriver = null;
    }

    private static void checkDriverLicense() {
        String driverLicense = checkingDriver.getDriverLicense();

        if (driverLicense == null || driverLicense.length() == 0) {
            throw new UserValidationException("Driver license cannot be blank");
        }

        Pattern pattern = Pattern.compile("\\d{10}");
        Matcher matcher = pattern.matcher(driverLicense);

        if (!matcher.matches()) {
            throw new UserValidationException("Incorrect driver license. Driver license can consist only 10 digits");
        }
    }

    private static void checkUser(boolean isUpdate) {
        UserDto user = checkingDriver.getUser();

        if (user == null) {
            throw new UserValidationException("UserDto property cannot be null");
        }

        UserValidator.validate(user, isUpdate);
    }
}
