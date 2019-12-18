package com.example.services;

import com.example.services.models.TruckDto;

import javax.validation.Valid;
import java.util.List;

public interface TruckService {

    TruckDto findById(Long truckId);

    /**
     * @param text     string that uses to search
     * @param page     number of page
     * @param pageSize size of page
     * @return list of {@link com.example.services.models.TruckDto}
     */
    List<TruckDto> getTrucks(String text, int page, int pageSize);

    /**
     * @param weight   min capacity of truck
     * @param text     string that uses to search
     * @param page     number of page
     * @param pageSize size of page
     * @return list of {@link com.example.services.models.TruckDto}
     */
    List<TruckDto> getFreeTrucks(double weight, String text, int page, int pageSize);

    TruckDto getFreeTruck(Long truckId, Long orderId, double weight);

    boolean isRegistrationNumberExists(String registrationNumber, Long truckId);

    /**
     * Check if can update truck
     *
     * @return true if can update
     */
    boolean canUpdateTruck(Long truckId);

    /**
     * @param truckDto {@link com.example.services.models.TruckDto}
     * @return true if truck successfully updated
     */
    boolean updateTruck(@Valid TruckDto truckDto);

    /**
     * @param truckDto {@link com.example.services.models.TruckDto}
     * @return true if truck successfully added
     */
    boolean addTruck(@Valid TruckDto truckDto);

    boolean setBrokenStatus(Long truckId);

    boolean setServiceableStatus(Long truckId);
}
