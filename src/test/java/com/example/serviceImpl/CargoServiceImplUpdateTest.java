package com.example.serviceImpl;

import com.example.controllers.exceptions.SavingCargoException;
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
import com.example.services.mappers.CargoMapperImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CargoServiceImplUpdateTest {

    private CargoMapper cargoMapper = new CargoMapperImpl();

    @InjectMocks
    private CargoService cargoService = new CargoServiceImpl(cargoMapper);

    @Mock
    private CargoRepository cargoRepository;

    @Mock
    private DriverService driverService;

    @Mock
    private TruckService truckService;

    @Test
    public void updateCargoSuccessfully() {
        CargoDto savingCargo = new CargoDto();
        savingCargo.setId(12);
        savingCargo.setWeight(300);
        DriverDto firstDriver = new DriverDto();
        firstDriver.setId(3);
        DriverDto coDriver = new DriverDto();
        coDriver.setId(7);
        TruckDto truck = new TruckDto();
        truck.setId(2);
        savingCargo.setDriverDto(firstDriver);
        savingCargo.setCoDriverDto(coDriver);
        savingCargo.setTruckDto(truck);

        CargoDto existCargo = new CargoDto();
        existCargo.setId(savingCargo.getId());
        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);

        DriverDto existFirstDriver = new DriverDto();
        existFirstDriver.setId(firstDriver.getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        DriverDto existCoDriver = new DriverDto();
        existCoDriver.setId(coDriver.getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(truck.getId());
        existTruck.setCondition(TruckCondition.SERVICEABLE);
        existTruck.setCapacity(700);

        Mockito
                .when(cargoRepository.findById(savingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));
        Mockito
                .when(driverService.findById(savingCargo.getDriverDto().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.findById(savingCargo.getCoDriverDto().getId()))
                .thenReturn(existCoDriver);
        Mockito
                .when(truckService.findById(savingCargo.getTruckDto().getId()))
                .thenReturn(existTruck);

        boolean result = cargoService.updateCargo(savingCargo);

        assertEquals(savingCargo.getDriverDto(), existFirstDriver);
        assertEquals(savingCargo.getCoDriverDto(), existCoDriver);
        assertEquals(savingCargo.getTruckDto(), existTruck);
        assertTrue(result);
    }

    @Test
    public void failedUpdateWrongCargoStatus() {
        CargoDto savingCargo = new CargoDto();
        savingCargo.setId(12);

        CargoDto existCargo = new CargoDto();
        existCargo.setId(savingCargo.getId());
        existCargo.setStatus(CargoStatus.DELIVERED);

        Mockito
                .when(cargoRepository.findById(savingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));

        SavingCargoException thrown = assertThrows(SavingCargoException.class,
                () -> cargoService.updateCargo(savingCargo));

        assertTrue(thrown.getMessage().contains("Cargo status must be REFUSED_BY_DRIVER"));
    }

    @Test
    public void failedUpdateEqualsDriverIds() {
        CargoDto savingCargo = new CargoDto();
        savingCargo.setId(12);
        DriverDto firstDriver = new DriverDto();
        firstDriver.setId(2);
        savingCargo.setDriverDto(firstDriver);
        DriverDto coDriver = new DriverDto();
        coDriver.setId(2);
        savingCargo.setCoDriverDto(coDriver);

        CargoDto existCargo = new CargoDto();
        existCargo.setId(savingCargo.getId());
        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);

        Mockito
                .when(cargoRepository.findById(savingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));

        SavingCargoException thrown = assertThrows(SavingCargoException.class,
                () -> cargoService.updateCargo(savingCargo));

        assertTrue(thrown.getMessage().contains("Driver id and co-driver id cannot be equals"));
    }

    @Test
    public void failedUpdateWrongFirstDriverStatus() {
        CargoDto savingCargo = new CargoDto();
        savingCargo.setId(12);
        DriverDto firstDriver = new DriverDto();
        firstDriver.setId(2);
        savingCargo.setDriverDto(firstDriver);
        DriverDto coDriver = new DriverDto();
        coDriver.setId(4);
        savingCargo.setCoDriverDto(coDriver);

        CargoDto existCargo = new CargoDto();
        existCargo.setId(savingCargo.getId());
        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);

        DriverDto existFirstDriver = new DriverDto();
        existFirstDriver.setId(firstDriver.getId());
        existFirstDriver.setStatus(DriverStatus.ACTIVE);

        Mockito
                .when(cargoRepository.findById(savingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));
        Mockito
                .when(driverService.findById(savingCargo.getDriverDto().getId()))
                .thenReturn(existFirstDriver);

        SavingCargoException thrown = assertThrows(SavingCargoException.class,
                () -> cargoService.updateCargo(savingCargo));

        assertTrue(thrown.getMessage().contains("Driver status must be equals REST"));
    }

    @Test
    public void failedUpdateWrongCoDriverStatus() {
        CargoDto savingCargo = new CargoDto();
        savingCargo.setId(12);
        DriverDto firstDriver = new DriverDto();
        firstDriver.setId(2);
        savingCargo.setDriverDto(firstDriver);
        DriverDto coDriver = new DriverDto();
        coDriver.setId(5);
        savingCargo.setCoDriverDto(coDriver);

        CargoDto existCargo = new CargoDto();
        existCargo.setId(savingCargo.getId());
        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);

        DriverDto existFirstDriver = new DriverDto();
        existFirstDriver.setId(firstDriver.getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        DriverDto existCoDriver = new DriverDto();
        existCoDriver.setId(coDriver.getId());
        existCoDriver.setStatus(DriverStatus.WAITING_FOR_MAIN_DRIVER_DECISION);

        Mockito
                .when(cargoRepository.findById(savingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));
        Mockito
                .when(driverService.findById(savingCargo.getDriverDto().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.findById(savingCargo.getCoDriverDto().getId()))
                .thenReturn(existCoDriver);

        SavingCargoException thrown = assertThrows(SavingCargoException.class,
                () -> cargoService.updateCargo(savingCargo));

        assertTrue(thrown.getMessage().contains("Co-Driver status must be equals REST"));
    }

    @Test
    public void failedUpdateWrongTruckStatus() {
        CargoDto savingCargo = new CargoDto();
        savingCargo.setId(12);
        DriverDto firstDriver = new DriverDto();
        firstDriver.setId(2);
        savingCargo.setDriverDto(firstDriver);
        DriverDto coDriver = new DriverDto();
        coDriver.setId(5);
        savingCargo.setCoDriverDto(coDriver);
        TruckDto truck = new TruckDto();
        truck.setId(15);
        savingCargo.setTruckDto(truck);

        CargoDto existCargo = new CargoDto();
        existCargo.setId(savingCargo.getId());
        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);

        DriverDto existFirstDriver = new DriverDto();
        existFirstDriver.setId(firstDriver.getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        DriverDto existCoDriver = new DriverDto();
        existCoDriver.setId(coDriver.getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(truck.getId());
        existTruck.setCondition(TruckCondition.FAULTY);
        existTruck.setCapacity(700);

        Mockito
                .when(cargoRepository.findById(savingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));
        Mockito
                .when(driverService.findById(savingCargo.getDriverDto().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.findById(savingCargo.getCoDriverDto().getId()))
                .thenReturn(existCoDriver);
        Mockito
                .when(truckService.findById(savingCargo.getTruckDto().getId()))
                .thenReturn(existTruck);

        SavingCargoException thrown = assertThrows(SavingCargoException.class,
                () -> cargoService.updateCargo(savingCargo));

        assertTrue(thrown.getMessage().contains("Truck condition must be equals SERVICEABLE"));
    }

    @Test
    public void failedUpdateWrongTruckCapacity() {
        CargoDto savingCargo = new CargoDto();
        savingCargo.setId(12);
        savingCargo.setWeight(1000);
        DriverDto firstDriver = new DriverDto();
        firstDriver.setId(2);
        savingCargo.setDriverDto(firstDriver);
        DriverDto coDriver = new DriverDto();
        coDriver.setId(5);
        savingCargo.setCoDriverDto(coDriver);
        TruckDto truck = new TruckDto();
        truck.setId(15);
        savingCargo.setTruckDto(truck);

        CargoDto existCargo = new CargoDto();
        existCargo.setId(savingCargo.getId());
        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);

        DriverDto existFirstDriver = new DriverDto();
        existFirstDriver.setId(firstDriver.getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        DriverDto existCoDriver = new DriverDto();
        existCoDriver.setId(coDriver.getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(truck.getId());
        existTruck.setCondition(TruckCondition.SERVICEABLE);
        existTruck.setCapacity(400);

        Mockito
                .when(cargoRepository.findById(savingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));
        Mockito
                .when(driverService.findById(savingCargo.getDriverDto().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.findById(savingCargo.getCoDriverDto().getId()))
                .thenReturn(existCoDriver);
        Mockito
                .when(truckService.findById(savingCargo.getTruckDto().getId()))
                .thenReturn(existTruck);

        SavingCargoException thrown = assertThrows(SavingCargoException.class,
                () -> cargoService.updateCargo(savingCargo));

        assertTrue(thrown.getMessage().contains("Cargo weight cannot be less than truck capacity"));
    }

    @Test
    public void failedUpdateTruckInOtherCargo() {
        CargoDto savingCargo = new CargoDto();
        savingCargo.setId(12);
        savingCargo.setWeight(1000);
        DriverDto firstDriver = new DriverDto();
        firstDriver.setId(2);
        savingCargo.setDriverDto(firstDriver);
        DriverDto coDriver = new DriverDto();
        coDriver.setId(5);
        savingCargo.setCoDriverDto(coDriver);
        TruckDto truck = new TruckDto();
        truck.setId(15);
        savingCargo.setTruckDto(truck);

        CargoDto existCargo = new CargoDto();
        existCargo.setId(savingCargo.getId());
        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);

        DriverDto existFirstDriver = new DriverDto();
        existFirstDriver.setId(firstDriver.getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        DriverDto existCoDriver = new DriverDto();
        existCoDriver.setId(coDriver.getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(truck.getId());
        existTruck.setCondition(TruckCondition.SERVICEABLE);
        existTruck.setCapacity(1200);

        CargoDto anotherCargo = new CargoDto();
        anotherCargo.setId(8);
        anotherCargo.setTruckDto(existTruck);

        Mockito
                .when(cargoRepository.findById(savingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));
        Mockito
                .when(driverService.findById(savingCargo.getDriverDto().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.findById(savingCargo.getCoDriverDto().getId()))
                .thenReturn(existCoDriver);
        Mockito
                .when(truckService.findById(savingCargo.getTruckDto().getId()))
                .thenReturn(existTruck);
        Mockito
                .when(cargoRepository.getCargoByTruckId(savingCargo.getTruckDto().getId()))
                .thenReturn(cargoMapper.fromDto(anotherCargo));

        SavingCargoException thrown = assertThrows(SavingCargoException.class,
                () -> cargoService.updateCargo(savingCargo));

        assertTrue(thrown.getMessage().contains("Truck cannot be include in other cargo"));
    }
}
