package com.example.serviceImpl;

import com.example.database.models.commons.CargoStatus;
import com.example.database.models.commons.DriverStatus;
import com.example.database.repositories.CargoRepository;
import com.example.models.CargoDto;
import com.example.services.CargoService;
import com.example.services.mappers.CargoMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class CargoServiceImpl implements CargoService {

    private final CargoMapper cargoMapper;
    private final CargoRepository cargoRepository;

    public CargoServiceImpl(CargoMapper cargoMapper, CargoRepository cargoRepository) {
        this.cargoMapper = cargoMapper;
        this.cargoRepository = cargoRepository;
    }

    @Override
    public CargoDto findById(int cargoId) {
        return cargoMapper.toDto(cargoRepository.findById(cargoId).get());
    }

    @Override
    public List<CargoDto> findAll() {
        List<CargoDto> cargoDtos = new ArrayList<>();
        cargoRepository.findAll().forEach(cargo -> cargoDtos.add(cargoMapper.toDto(cargo)));

        return cargoDtos;
    }

    @Override
    public CargoDto updateCargo(@Valid CargoDto cargoDto) {
        return cargoMapper.toDto(cargoRepository.save(cargoMapper.fromDto(cargoDto)));
    }

    @Override
    public void addCargo(@Valid CargoDto cargoDto) {
        cargoDto.setStatus(CargoStatus.CREATED);
        cargoRepository.save(cargoMapper.fromDto(cargoDto));
    }

    @Override
    public CargoDto getCargoByDriverId(int driverId) {
        return cargoMapper.toDto(cargoRepository.getCargoByDriverId(driverId));
    }

    @Override
    public void setAcceptStatus(int cargoId, int driverId) {
        CargoDto cargoDto = findById(cargoId);

        if (cargoDto.getDriverDto().getId() == driverId) {
            cargoDto.setStatus(CargoStatus.IN_PROGRESS);
            cargoDto.getDriverDto().setStatus(DriverStatus.ACTIVE);
            cargoDto.getCoDriverDto().setStatus(DriverStatus.ACTIVE);
            updateCargo(cargoDto);
        }
    }

    @Override
    public void setRefuseStatus(int cargoId, int driverId) {
        CargoDto cargoDto = findById(cargoId);

        if (cargoDto.getDriverDto().getId() == driverId) {
            cargoDto.setStatus(CargoStatus.REFUSED_BY_DRIVER);
            updateCargo(cargoDto);
        }
    }

    @Override
    public void setDeliverStatus(int cargoId, int driverId) {
        CargoDto cargoDto = findById(cargoId);

        if (cargoDto.getDriverDto().getId() == driverId) {
            cargoDto.setStatus(CargoStatus.DELIVERED);
            cargoDto.getDriverDto().setStatus(DriverStatus.REST);
            cargoDto.getCoDriverDto().setStatus(DriverStatus.REST);
            updateCargo(cargoDto);
        }

    }
}
