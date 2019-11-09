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
import com.example.services.CargoService;
import com.example.services.DriverService;
import com.example.services.TruckService;
import com.example.services.mappers.CargoMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class CargoServiceImpl implements CargoService {

    private final CargoMapper cargoMapper;
    private final CargoRepository cargoRepository;
    private final DriverService driverService;
    private final TruckService truckService;

    public CargoServiceImpl(CargoMapper cargoMapper, CargoRepository cargoRepository,
                            DriverService driverService, TruckService truckService) {
        this.cargoMapper = cargoMapper;
        this.cargoRepository = cargoRepository;
        this.driverService = driverService;
        this.truckService = truckService;
    }

    @Override
    public CargoDto findById(int cargoId) {
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
    public boolean updateCargo(@Valid CargoDto cargoDto) {
        checkSavingCargo(cargoDto, true);
        //cargoRepository.save(cargoMapper.fromDto(cargoDto));

        return true;
    }

    @Override
    public boolean addCargo(@Valid CargoDto cargoDto) {
        checkSavingCargo(cargoDto, false);
        cargoDto.setId(0);
        cargoDto.setStatus(CargoStatus.CREATED);
        cargoDto.getDriverDto().setStatus(DriverStatus.WAITING_FOR_MAIN_DRIVER_DECISION);
        cargoDto.getCoDriverDto().setStatus(DriverStatus.WAITING_FOR_MAIN_DRIVER_DECISION);
        //cargoRepository.save(cargoMapper.fromDto(cargoDto));

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
    public boolean setAcceptStatus(int cargoId, int driverId) {
        CargoDto cargoDto = getCheckedCargoToChangeStatus(cargoId, driverId);

        if (!cargoDto.getStatus().equals(CargoStatus.CREATED)) {
            throw new ChangeCargoStatusException("Attempt to set ACCEPT status to wrong cargo");
        }

        cargoDto.setStatus(CargoStatus.IN_PROGRESS);
        cargoDto.getDriverDto().setStatus(DriverStatus.ACTIVE);
        cargoDto.getCoDriverDto().setStatus(DriverStatus.ACTIVE);
        updateCargo(cargoDto);

        return true;
    }

    @Override
    public boolean setRefuseStatus(int cargoId, int driverId) {
        CargoDto cargoDto = getCheckedCargoToChangeStatus(cargoId, driverId);

        if (!cargoDto.getStatus().equals(CargoStatus.CREATED)) {
            throw new ChangeCargoStatusException("Attempt to set REFUSED_BY_DRIVER status to wrong cargo");
        }

        cargoDto.setStatus(CargoStatus.REFUSED_BY_DRIVER);
        cargoDto.getDriverDto().setStatus(DriverStatus.REST);
        cargoDto.getCoDriverDto().setStatus(DriverStatus.REST);
        updateCargo(cargoDto);

        return true;
    }

    @Override
    public boolean setDeliverStatus(int cargoId, int driverId) {
        CargoDto cargoDto = getCheckedCargoToChangeStatus(cargoId, driverId);

        if (!cargoDto.getStatus().equals(CargoStatus.IN_PROGRESS)) {
            throw new ChangeCargoStatusException("Attempt to set IN_PROGRESS status to wrong cargo");
        }

        cargoDto.setStatus(CargoStatus.DELIVERED);
        cargoDto.getDriverDto().setStatus(DriverStatus.REST);
        cargoDto.getCoDriverDto().setStatus(DriverStatus.REST);
        updateCargo(cargoDto);

        return true;
    }

    @Override
    public CargoDto getCargoByTruckId(int truckId) {
        return cargoMapper.toDto(cargoRepository.getCargoByTruckId(truckId));
    }

    private CargoDto getCheckedCargoToChangeStatus(int cargoId, int driverId) {
        CargoDto cargoDto = findById(cargoId);

        if (cargoDto.getDriverDto().getId() == driverId) {
            return cargoDto;
        }

        if (cargoDto.getCoDriverDto().getId() == driverId) {
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
        checkTruck(savingCargo);
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

        if (savingCargo.getCoDriverDto().getId() == savingCargo.getDriverDto().getId()) {
            exception.append("Driver id and co-driver id cannot be equals. Driver id: ");
            exception.append(savingCargo.getDriverDto().getId());
            exception.append(" , co-driver id: ");
            exception.append(savingCargo.getCoDriverDto().getId());
            throw new SavingCargoException(exception.toString());
        }
    }

    private void checkDrivers(CargoDto savingCargo) {
        StringBuilder exception = new StringBuilder();

        DriverDto driverDto = driverService.findById(savingCargo.getDriverDto().getId());
        if (!driverDto.getStatus().equals(DriverStatus.REST)) {
            exception.append("Driver status must be equals REST. Current status is: ");
            exception.append(driverDto.getStatus().name());
            throw new SavingCargoException(exception.toString());
        }

        DriverDto coDriverDto = driverService.findById(savingCargo.getCoDriverDto().getId());
        if (!coDriverDto.getStatus().equals(DriverStatus.REST)) {
            exception.append("Co-Driver status must be equals REST. Current status is: ");
            exception.append(coDriverDto.getStatus().name());
            throw new SavingCargoException(exception.toString());
        }

        savingCargo.setDriverDto(driverDto);
        savingCargo.setCoDriverDto(coDriverDto);
    }

    private void checkTruck(CargoDto savingCargo) {
        StringBuilder exception = new StringBuilder();
        CargoDto cargoDto = getCargoByTruckId(savingCargo.getTruckDto().getId());

        if (cargoDto != null) {
            exception.append("Truck cannot be include in other cargo. Truck is using by cargo with id: ");
            exception.append(cargoDto.getId());
            throw new SavingCargoException(exception.toString());
        }

        TruckDto truckDto = truckService.findById(savingCargo.getTruckDto().getId());
        if (!truckDto.getCondition().equals(TruckCondition.SERVICEABLE)) {
            exception.append("Truck condition must be equals SERVICEABLE");
            throw new SavingCargoException(exception.toString());
        }

        if (truckDto.getCapacity() < savingCargo.getWeight()) {
            exception.append("Cargo weight cannot be less than truck capacity");
            throw new SavingCargoException(exception.toString());
        }

        savingCargo.setTruckDto(truckDto);
    }
}
