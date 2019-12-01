package com.example.services.serviceImpl.validation;

import com.example.services.models.CargoDto;
import com.example.services.models.DriverDto;
import com.example.services.models.TruckDto;
import com.example.services.serviceImpl.validation.exception.CargoValidationException;

public class CargoValidator {
    private static CargoDto checkingCargo;

    public static void validate(CargoDto cargo) {
        checkingCargo = cargo;

        checkTitle();
        checkDescription();
        checkWeight();
        checkTruck();
        checkDriver();
        checkCoDriver();

        checkingCargo = null;
    }

    private static void checkTitle() {
        String title = checkingCargo.getTitle();

        if (title == null || title.length() == 0) {
            throw new CargoValidationException("Cargo title cannot be blank");
        }

        if (title.length() > 32) {
            throw new CargoValidationException("Title field length cannot be greater than 32");
        }
    }

    private static void checkDescription() {
        String description = checkingCargo.getDescription();

        if (description == null || description.length() == 0) {
            return;
        }

        if (description.length() > 256) {
            throw new CargoValidationException("Cargo description length cannot be greater than 256");
        }
    }

    private static void checkWeight() {
        double weight = checkingCargo.getWeight();

        if (weight <= 50) {
            throw new CargoValidationException("Cargo weight cannot be less than 50");
        }

        if (weight > 1000000) {
            throw new CargoValidationException("Cargo weight cannot be greater than 1000000");
        }
    }

    private static void checkTruck() {
/*        TruckDto truck = checkingCargo.getTruck();

        if (truck == null) {
            throw new CargoValidationException("Truck property cannot be null");
        }

        if (truck.getId() <= 0) {
            throw new CargoValidationException("Truck id cannot be equals or less than 0");
        }*/
    }

    private static void checkDriver() {
/*        DriverDto driver = checkingCargo.getDriver();

        if (driver == null) {
            throw new CargoValidationException("Driver property cannot be null");
        }

        if (driver.getId() <= 0) {
            throw new CargoValidationException("Driver id cannot be equals or less than 0");
        }*/
    }

    private static void checkCoDriver() {
/*        DriverDto coDriver = checkingCargo.getCoDriver();

        if (coDriver == null) {
            throw new CargoValidationException("Co-Driver property cannot be null");
        }

        if (coDriver.getId() <= 0) {
            throw new CargoValidationException("Co-Driver id cannot be equals or less than 0");
        }*/
    }
}
