package com.example.services.serviceImpl;

import com.example.controller.exceptions.ChangeOrderStatusException;
import com.example.database.models.Cargo;
import com.example.database.models.commons.CargoStatus;
import com.example.database.repositories.CargoRepository;
import com.example.services.CargoService;
import com.example.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.services.commons.message.CargoExceptionMessage.SET_STATUS_ERROR;

@Service
public class CargoServiceImpl implements CargoService {

    private CargoRepository cargoRepository;
    private OrderService orderService;

    public CargoServiceImpl() {
    }

    @Autowired
    public CargoServiceImpl(CargoRepository cargoRepository, OrderService orderService) {
        this.cargoRepository = cargoRepository;
        this.orderService = orderService;
    }

    @Override
    public boolean setDeliverStatus(Long cargoId, Long orderId, Long driverId) {
        Optional<Cargo> cargoOpt = cargoRepository.getCargoToDeliver(orderId, cargoId, driverId);
        Cargo cargo;

        if (cargoOpt.isEmpty()) {
            throw new ChangeOrderStatusException(SET_STATUS_ERROR);
        } else {
            cargo = cargoOpt.get();
        }

        cargo.setStatus(CargoStatus.DELIVERED);
        cargoRepository.save(cargo);
        orderService.tryToSetDeliverStatus(orderId);
        return true;
    }
}
