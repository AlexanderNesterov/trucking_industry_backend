package com.example.services;

public interface CargoService {

    /**
     * Setting deliver status to cargo
     * @return true if deliver status successfully set
     */
    boolean setDeliverStatus(Long cargoId, Long orderId, Long driverId);
}
