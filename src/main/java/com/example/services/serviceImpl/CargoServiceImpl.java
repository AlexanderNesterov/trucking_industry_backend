package com.example.services.serviceImpl;

import com.example.controller.exceptions.ChangeOrderStatusException;
import com.example.database.models.Cargo;
import com.example.database.models.City;
import com.example.database.models.commons.CargoStatus;
import com.example.database.repositories.CargoRepository;
import com.example.services.CargoService;
import com.example.services.OrderService;
import com.example.services.TruckService;
import com.example.services.mappers.CargoMapper;
import com.example.services.models.CargoDto;
import com.example.services.models.TruckDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.services.commons.message.CargoExceptionMessage.SET_STATUS_ERROR;

@Service
public class CargoServiceImpl implements CargoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CargoServiceImpl.class);
    public static final double delta = 0.06;

    private CargoMapper cargoMapper;
    private CargoRepository cargoRepository;
    private OrderService orderService;
    private TruckService truckService;

    public CargoServiceImpl() {
    }

    @Autowired
    public CargoServiceImpl(CargoMapper cargoMapper, CargoRepository cargoRepository,
                            OrderService orderService, TruckService truckService) {
        this.cargoMapper = cargoMapper;
        this.cargoRepository = cargoRepository;
        this.orderService = orderService;
        this.truckService = truckService;
    }

    @Override
    public boolean setDeliverStatus(Long cargoId, Long orderId, Long driverId) {
        Optional<Cargo> cargoOpt = cargoRepository.getCargoToDeliver(orderId, cargoId, driverId);
        TruckDto truck = truckService.getTruckByOrderId(orderId);
        Cargo cargo;

        if (cargoOpt.isEmpty() || truck == null || !checkCoordinates(cargoOpt.get(), truck)) {
            LOGGER.warn(SET_STATUS_ERROR);
            throw new ChangeOrderStatusException(SET_STATUS_ERROR);
        } else {
            cargo = cargoOpt.get();
        }

        cargo.setStatus(CargoStatus.DELIVERED);
        cargoRepository.save(cargo);
        orderService.tryToSetDeliverStatus(orderId);
        truckService.setZeroCoordinates(truck.getId());
        LOGGER.info("Cargo with id: {} set status {}", cargoId, CargoStatus.DELIVERED);
        return true;
    }

    @Override
    public List<CargoDto> getCargoListByOrderId(Long orderId) {
        return cargoMapper.toListDto(cargoRepository.getCargoByOrderId(orderId));
    }

    private boolean checkCoordinates(Cargo cargo, TruckDto truck) {
        City dischargeLocation = cargo.getDischargeLocation();
        return Math.abs(dischargeLocation.getLatitude() - truck.getLatitude()) < delta || Math.abs(dischargeLocation.getLongitude() - truck.getLongitude()) < delta;
    }
}
