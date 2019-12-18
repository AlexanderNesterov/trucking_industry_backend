package com.example.services.models;

import com.example.database.models.commons.TruckCondition;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

import static com.example.services.serviceImpl.validation.Message.*;

public class TruckDto {

    private Long id;

    @NotBlank(message = REGISTRATION_NUMBER + IS_BLANK)
    @Pattern(regexp = "[A-Z]{2}\\d{5}", message = REGISTRATION_NUMBER + INVALID_FORMAT)
    private String registrationNumber;

    @NotBlank(message = MODEL + IS_BLANK)
    @Length(max = 32, message = MODEL + TOO_LONG)
    private String model;

    @NotNull(message = CAPACITY + IS_NULL)
    @DecimalMin(value = "1", message = CAPACITY + TOO_SMALL + MIN_CAPACITY)
    @DecimalMax(value = "1000000", message = CAPACITY + TOO_BIG + MAX_CAPACITY)
    private double capacity;
    private TruckCondition condition;
    private String searchString;

    public TruckDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public TruckCondition getCondition() {
        return condition;
    }

    public void setCondition(TruckCondition condition) {
        this.condition = condition;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
