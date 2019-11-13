package com.example.services;

import com.example.models.CargoDto;
import java.util.List;

public interface CargoService {

    CargoDto findById(int cargoId);
    List<CargoDto> findAll();
    CargoDto getCargoByDriverId(int driverId);
    CargoDto getCargoByTruckId(int truckId);
    boolean addCargo(CargoDto cargoDto);
    boolean updateCargo(CargoDto cargoDto);
    boolean setAcceptStatus(int cargoId, int driverId);
    boolean setRefuseStatus(int cargoId, int driverId);
    boolean setDeliverStatus(int cargoId, int driverId);
}
