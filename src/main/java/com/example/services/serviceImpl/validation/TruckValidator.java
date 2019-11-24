package com.example.services.serviceImpl.validation;

import com.example.services.models.TruckDto;
import com.example.services.serviceImpl.validation.exception.TruckValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TruckValidator {
    private static TruckDto checkingTruck;

    public static void validate(TruckDto truck) {
        checkingTruck = truck;
        checkRegistrationNumber();
        checkModel();
        checkCapacity();

        checkingTruck = null;
    }

    private static void checkRegistrationNumber() {
        String registrationNumber = checkingTruck.getRegistrationNumber();

        if (registrationNumber == null || registrationNumber.length() == 0) {
            throw new TruckValidationException("Registration number field cannot be blank");
        }

        Pattern pattern = Pattern.compile("[A-Z]{2}\\d{5}");
        Matcher matcher = pattern.matcher(registrationNumber);

        if (!matcher.matches()) {
            throw new TruckValidationException("Incorrect registration number. Example: AA12345");
        }
    }

    private static void checkModel() {
        String model = checkingTruck.getModel();

        if (model == null || model.length() == 0) {
            throw new TruckValidationException("Model field cannot be blank");
        }

        if (model.length() > 32) {
            throw new TruckValidationException("Model field length cannot be greater than 32");
        }
    }

    private static void checkCapacity() {
        double capacity = checkingTruck.getCapacity();

        if (capacity <= 50) {
            throw new TruckValidationException("Capacity cannot be equals or less than 50");
        }

        if (capacity > 1000000) {
            throw new TruckValidationException("Capacity cannot be greater than 1000000");
        }
    }
}
