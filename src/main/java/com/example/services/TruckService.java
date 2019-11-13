package com.example.services;

import com.example.models.TruckDto;
import javax.validation.Valid;
import java.util.List;

public interface TruckService {

    TruckDto findById(int truckDtoId);
    List<TruckDto> findAll();
    boolean updateTruck(TruckDto truckDto);
    List<TruckDto> getFreeTrucks(double weight);
    boolean addTruck(TruckDto truckDto);
}
