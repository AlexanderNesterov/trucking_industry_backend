package com.example.services;

import com.example.services.models.TruckDto;

import javax.validation.Valid;
import java.util.List;

public interface TruckService {

    TruckDto findById(Long truckDtoId);
    List<TruckDto> getTrucks(String text, int page, int pageSize);
    List<TruckDto> getFreeTrucks(double weight);
    TruckDto getFreeTruck(Long truckId, double weight);
    boolean updateTruck(@Valid TruckDto truckDto);
    boolean addTruck(@Valid TruckDto truckDto);
}
