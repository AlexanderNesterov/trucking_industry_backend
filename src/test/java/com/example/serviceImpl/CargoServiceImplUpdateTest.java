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
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.JVM)
public class CargoServiceImplUpdateTest {

    private CargoMapper cargoMapper = new CargoMapperImpl();
    private CargoDto updatingCargo;

    @InjectMocks
    private CargoService cargoService = new CargoServiceImpl(cargoMapper);

    @Mock
    private CargoRepository cargoRepository;

    @Mock
    private DriverService driverService;

    @Mock
    private TruckService truckService;

    @Before
    public void setUp() {
        updatingCargo = new CargoDto();
        updatingCargo.setTitle("Water");
        updatingCargo.setWeight(300);
        TruckDto truck = new TruckDto();
        truck.setId(2);
        DriverDto driver = new DriverDto();
        driver.setId(4);
        DriverDto coDriver = new DriverDto();
        coDriver.setId(5);
        updatingCargo.setTruck(truck);
        updatingCargo.setDriver(driver);
        updatingCargo.setCoDriver(coDriver);
    }

    @Test
    public void updateCargoSuccessfully() {
        CargoDto existCargo = new CargoDto();
        existCargo.setId(updatingCargo.getId());
        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);

        DriverDto existFirstDriver = new DriverDto();
        existFirstDriver.setId(updatingCargo.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        DriverDto existCoDriver = new DriverDto();
        existCoDriver.setId(updatingCargo.getCoDriver().getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(updatingCargo.getTruck().getId());
        existTruck.setCondition(TruckCondition.SERVICEABLE);
        existTruck.setCapacity(700);

        Mockito
                .when(cargoRepository.findById(updatingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));
        Mockito
                .when(driverService.findById(updatingCargo.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.findById(updatingCargo.getCoDriver().getId()))
                .thenReturn(existCoDriver);
        Mockito
                .when(truckService.findById(updatingCargo.getTruck().getId()))
                .thenReturn(existTruck);

        boolean result = cargoService.updateCargo(updatingCargo);

        assertEquals(updatingCargo.getDriver(), existFirstDriver);
        assertEquals(updatingCargo.getCoDriver(), existCoDriver);
        assertEquals(updatingCargo.getTruck(), existTruck);
        assertTrue(result);
    }

    @Test
    public void failedUpdateWrongCargoStatus() {
        CargoDto existCargo = new CargoDto();
        existCargo.setId(updatingCargo.getId());
        existCargo.setStatus(CargoStatus.DELIVERED);

        Mockito
                .when(cargoRepository.findById(updatingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));

        SavingCargoException thrown = assertThrows(SavingCargoException.class,
                () -> cargoService.updateCargo(updatingCargo));

        assertTrue(thrown.getMessage().contains("Cargo status must be REFUSED_BY_DRIVER"));
    }

    @Test
    public void failedUpdateEqualsDriverIds() {
        updatingCargo.getDriver().setId(updatingCargo.getCoDriver().getId());

        CargoDto existCargo = new CargoDto();
        existCargo.setId(updatingCargo.getId());
        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);

        Mockito
                .when(cargoRepository.findById(updatingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));

        SavingCargoException thrown = assertThrows(SavingCargoException.class,
                () -> cargoService.updateCargo(updatingCargo));

        assertTrue(thrown.getMessage().contains("Driver id and co-driver id cannot be equals"));
    }

    @Test
    public void failedUpdateWrongFirstDriverStatus() {
        updatingCargo.getDriver().setId(4);

        CargoDto existCargo = new CargoDto();
        existCargo.setId(updatingCargo.getId());
        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);

        DriverDto existFirstDriver = new DriverDto();
        existFirstDriver.setId(updatingCargo.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.ACTIVE);

        Mockito
                .when(cargoRepository.findById(updatingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));
        Mockito
                .when(driverService.findById(updatingCargo.getDriver().getId()))
                .thenReturn(existFirstDriver);

        SavingCargoException thrown = assertThrows(SavingCargoException.class,
                () -> cargoService.updateCargo(updatingCargo));

        assertTrue(thrown.getMessage().contains("Driver status must be equals REST"));
    }

    @Test
    public void failedUpdateWrongCoDriverStatus() {
        updatingCargo.getDriver().setId(4);

        CargoDto existCargo = new CargoDto();
        existCargo.setId(updatingCargo.getId());
        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);

        DriverDto existFirstDriver = new DriverDto();
        existFirstDriver.setId(updatingCargo.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        DriverDto existCoDriver = new DriverDto();
        existCoDriver.setId(updatingCargo.getCoDriver().getId());
        existCoDriver.setStatus(DriverStatus.ACTIVE);

        Mockito
                .when(cargoRepository.findById(updatingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));
        Mockito
                .when(driverService.findById(updatingCargo.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.findById(updatingCargo.getCoDriver().getId()))
                .thenReturn(existCoDriver);

        SavingCargoException thrown = assertThrows(SavingCargoException.class,
                () -> cargoService.updateCargo(updatingCargo));

        assertTrue(thrown.getMessage().contains("Co-Driver status must be equals REST"));
    }

    @Test
    public void failedUpdateWrongTruckStatus() {
        CargoDto existCargo = new CargoDto();
        existCargo.setId(updatingCargo.getId());
        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);

        DriverDto existFirstDriver = new DriverDto();
        existFirstDriver.setId(updatingCargo.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        DriverDto existCoDriver = new DriverDto();
        existCoDriver.setId(updatingCargo.getCoDriver().getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(updatingCargo.getTruck().getId());
        existTruck.setCondition(TruckCondition.FAULTY);
        existTruck.setCapacity(700);

        Mockito
                .when(cargoRepository.findById(updatingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));
        Mockito
                .when(driverService.findById(updatingCargo.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.findById(updatingCargo.getCoDriver().getId()))
                .thenReturn(existCoDriver);
        Mockito
                .when(truckService.findById(updatingCargo.getTruck().getId()))
                .thenReturn(existTruck);

        SavingCargoException thrown = assertThrows(SavingCargoException.class,
                () -> cargoService.updateCargo(updatingCargo));

        assertTrue(thrown.getMessage().contains("Truck condition must be equals SERVICEABLE"));
    }

    @Test
    public void failedUpdateWrongTruckCapacity() {
        updatingCargo.setWeight(1000);

        CargoDto existCargo = new CargoDto();
        existCargo.setId(updatingCargo.getId());
        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);

        DriverDto existFirstDriver = new DriverDto();
        existFirstDriver.setId(updatingCargo.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        DriverDto existCoDriver = new DriverDto();
        existCoDriver.setId(updatingCargo.getCoDriver().getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(updatingCargo.getTruck().getId());
        existTruck.setCondition(TruckCondition.SERVICEABLE);
        existTruck.setCapacity(400);

        Mockito
                .when(cargoRepository.findById(updatingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));
        Mockito
                .when(driverService.findById(updatingCargo.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.findById(updatingCargo.getCoDriver().getId()))
                .thenReturn(existCoDriver);
        Mockito
                .when(truckService.findById(updatingCargo.getTruck().getId()))
                .thenReturn(existTruck);

        SavingCargoException thrown = assertThrows(SavingCargoException.class,
                () -> cargoService.updateCargo(updatingCargo));

        assertTrue(thrown.getMessage().contains("Cargo weight cannot be less than truck capacity"));
    }

    @Test
    public void failedUpdateTruckInOtherCargo() {
        CargoDto existCargo = new CargoDto();
        existCargo.setId(updatingCargo.getId());
        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);

        DriverDto existFirstDriver = new DriverDto();
        existFirstDriver.setId(updatingCargo.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        DriverDto existCoDriver = new DriverDto();
        existCoDriver.setId(updatingCargo.getCoDriver().getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(updatingCargo.getTruck().getId());
        existTruck.setCondition(TruckCondition.SERVICEABLE);
        existTruck.setCapacity(1200);

        CargoDto anotherCargo = new CargoDto();
        anotherCargo.setId(8L);
        anotherCargo.setTruck(existTruck);

        Mockito
                .when(cargoRepository.findById(updatingCargo.getId()))
                .thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));
        Mockito
                .when(driverService.findById(updatingCargo.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.findById(updatingCargo.getCoDriver().getId()))
                .thenReturn(existCoDriver);
        Mockito
                .when(truckService.findById(updatingCargo.getTruck().getId()))
                .thenReturn(existTruck);
        Mockito
                .when(cargoRepository.getCargoByTruckId(updatingCargo.getTruck().getId()))
                .thenReturn(cargoMapper.fromDto(anotherCargo));

        SavingCargoException thrown = assertThrows(SavingCargoException.class,
                () -> cargoService.updateCargo(updatingCargo));

        assertTrue(thrown.getMessage().contains("Truck cannot be include in other cargo"));
    }
}
