package com.example.services;

public interface CargoService {

    /**
     * VNFHJGH
     * @param cargoId
     * @param orderId
     * @param driverId
     * @return
     */
    boolean setDeliverStatus(Long cargoId, Long orderId, Long driverId);
}
