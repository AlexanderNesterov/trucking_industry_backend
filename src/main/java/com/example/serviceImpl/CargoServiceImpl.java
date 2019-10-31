package com.example.serviceImpl;

import com.example.database.models.commons.CargoStatus;
import com.example.database.models.commons.DriverCargoStatus;
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
        cargoRepository.findAll().forEach(driver -> cargoDtos.add(cargoMapper.toDto(driver)));
        return cargoDtos;
    }

    @Override
    public CargoDto updateCargo(@Valid CargoDto cargoDto) {
        return cargoMapper.toDto(cargoRepository.save(cargoMapper.fromDto(cargoDto)));
    }

    @Override
    public void addCargo(@Valid CargoDto cargoDto) {
        cargoDto.setStatus(CargoStatus.CREATED);
        cargoDto.setDriverStatus(DriverCargoStatus.NOT_SELECTED);
        cargoDto.setCoDriverStatus(DriverCargoStatus.NOT_SELECTED);
        cargoRepository.save(cargoMapper.fromDto(cargoDto));
    }

    @Override
    public CargoDto getCargoByDriverId(int driverId) {
        return cargoMapper.toDto(cargoRepository.getCargoByDriverId(driverId));
    }

    @Override
    public void setAcceptStatus(int cargoId, int driverId) {
        CargoDto cargoDto = findById(cargoId);

        if (cargoDto.getDriver().getId() == driverId) {
            cargoDto.setDriverStatus(DriverCargoStatus.ACCEPT);
        } else if (cargoDto.getCoDriver().getId() == driverId) {
            cargoDto.setCoDriverStatus(DriverCargoStatus.ACCEPT);
        }

        updateCargo(cargoDto);
    }

    @Override
    public void setRefuseStatus(int cargoId, int driverId) {
        CargoDto cargoDto = findById(cargoId);

        if (cargoDto.getDriver().getId() == driverId) {
            cargoDto.setDriverStatus(DriverCargoStatus.REFUSE);
        } else if (cargoDto.getCoDriver().getId() == driverId) {
            cargoDto.setCoDriverStatus(DriverCargoStatus.REFUSE);
        }

        updateCargo(cargoDto);
    }

    @Override
    public void autoChangeCargoStatus() {
        List<CargoDto> cargoDtos = cargoMapper.toListDto(cargoRepository.getCreatedCargoList());

        for (CargoDto cargoDto : cargoDtos) {
            if (cargoDto.getDriverStatus() == DriverCargoStatus.ACCEPT &&
                    cargoDto.getCoDriverStatus() == DriverCargoStatus.ACCEPT) {
                cargoDto.setStatus(CargoStatus.IN_PROGRESS);
                cargoDto.getDriver().setStatus(DriverStatus.ACTIVE);
                cargoDto.getCoDriver().setStatus(DriverStatus.ACTIVE);
            } else if (cargoDto.getDriverStatus() == DriverCargoStatus.REFUSE ||
                    cargoDto.getCoDriverStatus() == DriverCargoStatus.REFUSE) {
                cargoDto.setStatus(CargoStatus.REFUSED_BY_DRIVER);
            }
        }

        if (cargoDtos.size() != 0) {
            cargoRepository.saveAll(cargoMapper.fromListDto(cargoDtos));
        }
    }
}
