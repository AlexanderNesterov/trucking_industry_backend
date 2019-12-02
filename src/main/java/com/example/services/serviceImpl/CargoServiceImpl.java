package com.example.services.serviceImpl;

import com.example.controller.exceptions.ChangeCargoStatusException;
import com.example.database.models.Cargo;
import com.example.database.models.commons.CargoStatus;
import com.example.database.repositories.CargoRepository;
import com.example.services.CargoService;
import com.example.services.OrderService;
import com.example.services.mappers.CargoMapper;
import org.springframework.stereotype.Service;

@Service
public class CargoServiceImpl implements CargoService {

    private final CargoRepository cargoRepository;
    private final OrderService orderService;
    private final CargoMapper cargoMapper;

    public CargoServiceImpl(CargoRepository cargoRepository, OrderService orderService,
                            CargoMapper cargoMapper) {
        this.cargoRepository = cargoRepository;
        this.orderService = orderService;
        this.cargoMapper = cargoMapper;
    }

    @Override
    public boolean setDeliverStatus(Long cargoId, Long orderId, Long driverId) {
        Cargo cargo = cargoRepository.getCargoToDeliver(orderId, cargoId, driverId);

        if (cargo == null) {
            throw new ChangeCargoStatusException("Order id or driver id or cargo id or " +
                    "cargo status or order status is wrong");
        }

        cargo.setStatus(CargoStatus.DELIVERED);
        cargoRepository.save(cargo);
        orderService.tryToSetDeliverStatus(orderId);

        return true;
    }
}
