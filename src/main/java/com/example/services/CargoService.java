package com.example.services;

public interface CargoService {

    boolean setDeliverStatus(Long cargoId, Long orderId, Long driverId);
}
