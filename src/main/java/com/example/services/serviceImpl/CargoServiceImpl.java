package com.example.services.serviceImpl;

import com.example.controller.exceptions.CargoNotFoundException;
import com.example.controller.exceptions.ChangeCargoStatusException;
import com.example.controller.exceptions.SavingCargoException;
import com.example.database.models.Cargo;
import com.example.database.models.Driver;
import com.example.database.models.commons.CargoStatus;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.TruckCondition;
import com.example.database.repositories.CargoRepository;
import com.example.services.models.CargoDto;
import com.example.services.models.DriverDto;
import com.example.services.models.TruckDto;
import com.example.services.serviceImpl.validation.CargoValidator;
import com.example.services.CargoService;
import com.example.services.DriverService;
import com.example.services.TruckService;
import com.example.services.mappers.CargoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CargoServiceImpl implements CargoService {

    private CargoMapper cargoMapper;
    private CargoRepository cargoRepository;
    private DriverService driverService;
    private TruckService truckService;

    public CargoServiceImpl(CargoMapper cargoMapper) {
        this.cargoMapper = cargoMapper;
    }

    @Autowired
    public CargoServiceImpl(CargoMapper cargoMapper, CargoRepository cargoRepository,
                            DriverService driverService, TruckService truckService) {
        this.cargoMapper = cargoMapper;
        this.cargoRepository = cargoRepository;
        this.driverService = driverService;
        this.truckService = truckService;
    }

    @Override
    public CargoDto findById(Long cargoId) {
        Optional<Cargo> cargo = cargoRepository.findById(cargoId);

        if (cargo.isPresent()) {
            return cargoMapper.toDto(cargo.get());
        } else {
            throw new CargoNotFoundException("Cargo with id: " + cargoId + " not found");
        }
    }

    @Override
    public List<CargoDto> findAll() {
        List<CargoDto> cargoDtos = new ArrayList<>();
        cargoRepository.findAll().forEach(cargo -> cargoDtos.add(cargoMapper.toDto(cargo)));

        return cargoDtos;
    }

    @Override
    public boolean updateCargo(CargoDto cargoDto) {
        CargoValidator.validate(cargoDto);
        checkSavingCargo(cargoDto, true);

        cargoDto.setStatus(CargoStatus.CREATED);
        cargoRepository.save(cargoMapper.fromDto(cargoDto));
        return true;
    }

    @Override
    public boolean addCargo(CargoDto cargoDto) {
        CargoValidator.validate(cargoDto);
        checkSavingCargo(cargoDto, false);

        cargoDto.setId(null);
        cargoDto.setStatus(CargoStatus.CREATED);
        cargoRepository.save(cargoMapper.fromDto(cargoDto));
        return true;
    }

    @Override
    public CargoDto getCargoByDriverId(Long driverId) {
        CargoDto cargoDto = cargoMapper.toDto(cargoRepository.getCargoByDriverId(driverId));

        if (cargoDto == null) {
            throw new CargoNotFoundException("Cargo with driver id: " + driverId + " not found");
        }

        return cargoDto;
    }

    @Override
    public boolean setAcceptStatus(Long cargoId, String driverLogin) {
        Cargo cargo = getCheckedCargoToChangeStatus(cargoId, driverLogin);

        if (!cargo.getStatus().equals(CargoStatus.CREATED)) {
            throw new ChangeCargoStatusException("Attempt to set ACCEPT status to wrong cargo");
        }

        cargo.setStatus(CargoStatus.IN_PROGRESS);
        cargo.getDriver().setStatus(DriverStatus.ACTIVE);
        cargo.getCoDriver().setStatus(DriverStatus.ACTIVE);
        cargoRepository.save(cargo);

        return true;
    }

    @Override
    public boolean setRefuseStatus(Long cargoId, String driverLogin) {
        Cargo cargo = getCheckedCargoToChangeStatus(cargoId, driverLogin);

        if (!cargo.getStatus().equals(CargoStatus.CREATED)) {
            throw new ChangeCargoStatusException("Attempt to set REFUSED_BY_DRIVER status to wrong cargo");
        }

        cargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);
        cargo.getDriver().setStatus(DriverStatus.REST);
        cargo.getCoDriver().setStatus(DriverStatus.REST);
        cargoRepository.save(cargo);

        return true;
    }

    @Override
    public boolean setDeliverStatus(Long cargoId, String driverLogin) {
        Cargo cargo = getCheckedCargoToChangeStatus(cargoId, driverLogin);

        if (!cargo.getStatus().equals(CargoStatus.IN_PROGRESS)) {
            throw new ChangeCargoStatusException("Attempt to set IN_PROGRESS status to wrong cargo");
        }

        cargo.setStatus(CargoStatus.DELIVERED);
        cargo.getDriver().setStatus(DriverStatus.REST);
        cargo.getCoDriver().setStatus(DriverStatus.REST);
        cargoRepository.save(cargo);

        return true;
    }

    private Cargo getCheckedCargoToChangeStatus(Long cargoId, String driverLogin) {
        Cargo cargo = cargoRepository.getCargoToChangeStatus(cargoId, driverLogin);

        if (cargo == null) {
            throw new ChangeCargoStatusException("Wrong cargo id or main driver id");
        }

        return cargo;
    }

    private void checkSavingCargo(CargoDto savingCargo, boolean isUpdate) {
        if (isUpdate) {
            checkCargo(savingCargo);
        }

        checkDriversIds(savingCargo);
        checkDrivers(savingCargo);
        checkTruck(savingCargo);
    }

    private void checkCargo(CargoDto savingCargo) {
        Cargo cargo = cargoRepository.getCargoToUpdate(savingCargo.getId());

        if (cargo == null) {
            throw new SavingCargoException("Wrong cargo id or cargo status not equals 'REFUSED_BY_DRIVER'");
        }
    }

    private void checkDriversIds(CargoDto savingCargo) {
        StringBuilder exception = new StringBuilder();

        if (savingCargo.getDriver().getId() == null || savingCargo.getCoDriver().getId() == null) {
            throw new SavingCargoException("Driver id and co-driver id cannot equals null");
        }
        if (savingCargo.getCoDriver().getId().equals(savingCargo.getDriver().getId())) {
            exception.append("Driver id and co-driver id cannot be equals. Driver id: ");
            exception.append(savingCargo.getDriver().getId());
            exception.append(", co-driver id: ");
            exception.append(savingCargo.getCoDriver().getId());
            throw new SavingCargoException(exception.toString());
        }
    }

    private void checkDrivers(CargoDto savingCargo) {
        DriverDto driver = driverService.getFreeDriver(savingCargo.getDriver().getId());
        if (driver == null) {
            throw new SavingCargoException("Wrong driver id or driver status");
        }

        DriverDto coDriver = driverService.getFreeDriver(savingCargo.getCoDriver().getId());
        if (coDriver == null) {
            throw new SavingCargoException("Wrong co-driver id or co-driver status");
        }

        savingCargo.setDriver(driver);
        savingCargo.setCoDriver(coDriver);
    }

    private void checkTruck(CargoDto savingCargo) {
        TruckDto truckDto = truckService.getFreeTruck(savingCargo.getTruck().getId(), savingCargo.getWeight());

        if (truckDto == null) {
            throw new SavingCargoException(
                    "Wrong truck id or truck condition or truck already include in another cargo");
        }

        savingCargo.setTruck(truckDto);
    }
}
