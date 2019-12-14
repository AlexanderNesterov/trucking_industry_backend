package com.example.services.models;

import com.example.database.models.commons.CargoStatus;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.*;
import static com.example.services.serviceImpl.validation.Message.*;

public class CargoDto {

    private Long id;

    @NotBlank(message = TITLE + IS_BLANK)
    @Length(max = 32, message = TITLE + TOO_LONG + TITLE_MAX_LENGTH)
    private String title;

    @Length(max = 256, message = DESCRIPTION + TOO_LONG + DESCRIPTION_MAX_LENGTH)
    private String description;

    private CityDto loadLocation;
    private CityDto dischargeLocation;

    @NotNull(message = WEIGHT + IS_NULL)
    @DecimalMin(value = "1", message = WEIGHT + TOO_SMALL + WEIGHT_MIN_VALUE)
    @DecimalMax(value = "1000000", message = WEIGHT + TOO_BIG + WEIGHT_MAX_VALUE)
    private double weight;

    private CargoStatus status;

    public CargoDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CityDto getLoadLocation() {
        return loadLocation;
    }

    public void setLoadLocation(CityDto loadLocation) {
        this.loadLocation = loadLocation;
    }

    public CityDto getDischargeLocation() {
        return dischargeLocation;
    }

    public void setDischargeLocation(CityDto dischargeLocation) {
        this.dischargeLocation = dischargeLocation;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public CargoStatus getStatus() {
        return status;
    }

    public void setStatus(CargoStatus status) {
        this.status = status;
    }
}
