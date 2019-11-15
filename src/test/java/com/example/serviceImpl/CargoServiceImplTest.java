package com.example.serviceImpl;

import com.example.controllers.exceptions.CargoNotFoundException;
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

@RunWith(MockitoJUnitRunner.class)
public class CargoServiceImplTest {

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
    public void findByIdSuccessfully() {
        CargoDto existCargo = new CargoDto();
        existCargo.setId(45L);
        existCargo.setStatus(CargoStatus.CREATED);

        Mockito.when(cargoRepository.findById(45L)).thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));
        CargoDto foundCargo = cargoService.findById(45L);

        assertEquals(existCargo.getStatus(), foundCargo.getStatus());
    }

    @Test
    public void failedFindCargoById() {
        Mockito.when(cargoRepository.findById(90L)).thenReturn(Optional.empty());

        CargoNotFoundException thrown = assertThrows(CargoNotFoundException.class,
                () -> cargoService.findById(90L));

        assertTrue(thrown.getMessage().contains("not found"));
    }


    @Test
    public void addCargoSuccessfully() {
        CargoDto savingCargo = new CargoDto();
        savingCargo.setTitle("Water");
        savingCargo.setWeight(300);
        DriverDto firstDriver = new DriverDto();
        firstDriver.setId(3L);
        DriverDto coDriver = new DriverDto();
        coDriver.setId(7L);
        TruckDto truck = new TruckDto();
        truck.setId(2);
        savingCargo.setDriver(firstDriver);
        savingCargo.setCoDriver(coDriver);
        savingCargo.setTruck(truck);

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
                .when(driverService.findById(savingCargo.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.findById(savingCargo.getCoDriver().getId()))
                .thenReturn(existCoDriver);
        Mockito
                .when(truckService.findById(savingCargo.getTruck().getId()))
                .thenReturn(existTruck);
        Mockito
                .when(cargoRepository.getCargoByTruckId(savingCargo.getTruck().getId()))
                .thenReturn(null);

        boolean result = cargoService.addCargo(savingCargo);

        assertEquals(existFirstDriver, savingCargo.getDriver());
        assertEquals(existCoDriver, savingCargo.getCoDriver());
        assertEquals(existTruck, savingCargo.getTruck());
        assertEquals(0, savingCargo.getId());
        assertEquals(CargoStatus.CREATED, savingCargo.getStatus());
        assertTrue(result);
    }

    @Test
    public void getCargoByDriverIdSuccessfully() {
        CargoDto existCargo = new CargoDto();
        existCargo.setId(5L);
        existCargo.setWeight(220);
        existCargo.setStatus(CargoStatus.IN_PROGRESS);

        Mockito.when(cargoRepository.findById(5L)).thenReturn(Optional.of(cargoMapper.fromDto(existCargo)));
        CargoDto foundCargo = cargoService.findById(5L);

        assertEquals(existCargo.getWeight(), foundCargo.getWeight());
        assertEquals(existCargo.getStatus(), foundCargo.getStatus());
    }

    @Test
    public void failedGetCargoByDriverId() {
        CargoNotFoundException thrown = assertThrows(CargoNotFoundException.class,
                () -> cargoService.getCargoByDriverId(12L));

        assertTrue(thrown.getMessage().contains("not found"));
    }
}
