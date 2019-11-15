package com.example.serviceImpl;

import com.example.controllers.exceptions.CargoNotFoundException;
import com.example.controllers.exceptions.ChangeCargoStatusException;
import com.example.controllers.exceptions.SavingCargoException;
import com.example.database.models.Cargo;
import com.example.database.models.commons.CargoStatus;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.TruckCondition;
import com.example.database.repositories.CargoRepository;
import com.example.models.CargoDto;
import com.example.models.DriverDto;
import com.example.models.TruckDto;
import com.example.serviceImpl.validation.CargoValidator;
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

        cargoDto.setId(0L);
        cargoDto.setStatus(CargoStatus.CREATED);
        cargoRepository.save(cargoMapper.fromDto(cargoDto));
        return true;
    }

    @Override
    public CargoDto getCargoByDriverId(int driverId) {
        CargoDto cargoDto = cargoMapper.toDto(cargoRepository.getCargoByDriverId(driverId));

        if (cargoDto == null) {
            throw new CargoNotFoundException("Cargo with driver id: " + driverId + " not found");
        }

        return cargoDto;
    }

    @Override
    public boolean setAcceptStatus(Long cargoId, int driverId) {
        CargoDto cargoDto = getCheckedCargoToChangeStatus(cargoId, driverId);

        if (!cargoDto.getStatus().equals(CargoStatus.CREATED)) {
            throw new ChangeCargoStatusException("Attempt to set ACCEPT status to wrong cargo");
        }

        cargoDto.setStatus(CargoStatus.IN_PROGRESS);
        cargoDto.getDriver().setStatus(DriverStatus.ACTIVE);
        cargoDto.getCoDriver().setStatus(DriverStatus.ACTIVE);
        cargoRepository.save(cargoMapper.fromDto(cargoDto));

        return true;
    }

    @Override
    public boolean setRefuseStatus(Long cargoId, int driverId) {
        CargoDto cargoDto = getCheckedCargoToChangeStatus(cargoId, driverId);

        if (!cargoDto.getStatus().equals(CargoStatus.CREATED)) {
            throw new ChangeCargoStatusException("Attempt to set REFUSED_BY_DRIVER status to wrong cargo");
        }

        cargoDto.setStatus(CargoStatus.REFUSED_BY_DRIVER);
        cargoDto.getDriver().setStatus(DriverStatus.REST);
        cargoDto.getCoDriver().setStatus(DriverStatus.REST);
        cargoRepository.save(cargoMapper.fromDto(cargoDto));

        return true;
    }

    @Override
    public boolean setDeliverStatus(Long cargoId, int driverId) {
        CargoDto cargoDto = getCheckedCargoToChangeStatus(cargoId, driverId);

        if (!cargoDto.getStatus().equals(CargoStatus.IN_PROGRESS)) {
            throw new ChangeCargoStatusException("Attempt to set IN_PROGRESS status to wrong cargo");
        }

        cargoDto.setStatus(CargoStatus.DELIVERED);
        cargoDto.getDriver().setStatus(DriverStatus.REST);
        cargoDto.getCoDriver().setStatus(DriverStatus.REST);
        cargoRepository.save(cargoMapper.fromDto(cargoDto));

        return true;
    }

    @Override
    public CargoDto getCargoByTruckId(int truckId) {
        return cargoMapper.toDto(cargoRepository.getCargoByTruckId(truckId));
    }

    private CargoDto getCheckedCargoToChangeStatus(Long cargoId, int driverId) {
        CargoDto cargoDto = findById(cargoId);

        if (cargoDto.getDriver().getId() == driverId) {
            return cargoDto;
        }

        if (cargoDto.getCoDriver().getId() == driverId) {
            throw new ChangeCargoStatusException("Driver with id: " +
                    driverId + " is not main driver for cargo with id: " + cargoId);
        } else {
            throw new ChangeCargoStatusException("Driver with id: " +
                    driverId + " is not included in cargo with id: " + cargoId);
        }
    }

    private void checkSavingCargo(CargoDto savingCargo, boolean isUpdate) {
        if (isUpdate) {
            checkCargo(savingCargo);
        }

        checkDriversIds(savingCargo);
        checkDrivers(savingCargo);
        checkTruck(savingCargo, isUpdate);
    }

    private void checkCargo(CargoDto savingCargo) {
        StringBuilder exception = new StringBuilder();
        CargoDto cargoDto = findById(savingCargo.getId());

        if (!cargoDto.getStatus().equals(CargoStatus.REFUSED_BY_DRIVER)) {
            exception.append("Cargo status must be REFUSED_BY_DRIVER. Current status: ");
            exception.append(cargoDto.getStatus());
            throw new SavingCargoException(exception.toString());
        }
    }

    private void checkDriversIds(CargoDto savingCargo) {
        StringBuilder exception = new StringBuilder();

        if (savingCargo.getCoDriver().getId() == savingCargo.getDriver().getId()) {
            exception.append("Driver id and co-driver id cannot be equals. Driver id: ");
            exception.append(savingCargo.getDriver().getId());
            exception.append(", co-driver id: ");
            exception.append(savingCargo.getCoDriver().getId());
            throw new SavingCargoException(exception.toString());
        }
    }

    private void checkDrivers(CargoDto savingCargo) {
        StringBuilder exception = new StringBuilder();

        DriverDto driverDto = driverService.findById(savingCargo.getDriver().getId());
        if (!driverDto.getStatus().equals(DriverStatus.REST)) {
            exception.append("Driver status must be equals REST. Current status is: ");
            exception.append(driverDto.getStatus().name());
            throw new SavingCargoException(exception.toString());
        }

        DriverDto coDriverDto = driverService.findById(savingCargo.getCoDriver().getId());
        if (!coDriverDto.getStatus().equals(DriverStatus.REST)) {
            exception.append("Co-Driver status must be equals REST. Current status is: ");
            exception.append(coDriverDto.getStatus().name());
            throw new SavingCargoException(exception.toString());
        }

        savingCargo.setDriver(driverDto);
        savingCargo.setCoDriver(coDriverDto);
    }

    private void checkTruck(CargoDto savingCargo, boolean isUpdate) {
        StringBuilder exception = new StringBuilder();

        TruckDto truckDto = truckService.findById(savingCargo.getTruck().getId());
        if (!truckDto.getCondition().equals(TruckCondition.SERVICEABLE)) {
            exception.append("Truck condition must be equals SERVICEABLE");
            throw new SavingCargoException(exception.toString());
        }

        if (truckDto.getCapacity() < savingCargo.getWeight()) {
            exception.append("Cargo weight cannot be less than truck capacity");
            throw new SavingCargoException(exception.toString());
        }

        CargoDto cargoDto = getCargoByTruckId(savingCargo.getTruck().getId());
        if (cargoDto == null || (isUpdate && cargoDto.getId() == savingCargo.getId())) {
            savingCargo.setTruck(truckDto);
            return;
        }

        exception.append("Truck cannot be include in other cargo. Truck is using by cargo with id: ");
        exception.append(cargoDto.getId());
        throw new SavingCargoException(exception.toString());
    }
}
