package com.example.services;

import com.example.services.models.CargoDto;
import java.util.List;

public interface CargoService {

    CargoDto findById(Long cargoId);
    List<CargoDto> findAll();
    CargoDto getCargoByDriverId(Long driverId);
    boolean addCargo(CargoDto cargoDto);
    boolean updateCargo(CargoDto cargoDto);
    boolean setAcceptStatus(Long cargoId, String driverLogin);
    boolean setRefuseStatus(Long cargoId, String driverLogin);
    boolean setDeliverStatus(Long cargoId, String driverLogin);
}
