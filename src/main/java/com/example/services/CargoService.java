package com.example.services;

import com.example.services.models.CargoDto;

import java.util.List;

public interface CargoService {

    List<CargoDto> getCargoListByOrderId(Long orderId);

    /**
     * Setting deliver status to cargo
     *
     * @return true if deliver status successfully set
     */
    boolean setDeliverStatus(Long cargoId, Long orderId, Long driverId);
}
