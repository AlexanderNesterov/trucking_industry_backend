//package com.example.services.serviceImpl;
//
//import com.example.controller.exceptions.SavingCargoException;
//import com.example.database.models.commons.CargoStatus;
//import com.example.database.models.commons.DriverStatus;
//import com.example.database.models.commons.TruckCondition;
//import com.example.database.repositories.CargoRepository;
//import com.example.services.mappers.CargoMapperImpl;
//import com.example.services.models.CargoDto;
//import com.example.services.models.DriverDto;
//import com.example.services.models.TruckDto;
//import com.example.services.CargoService;
//import com.example.services.DriverService;
//import com.example.services.TruckService;
//import com.example.services.mappers.CargoMapper;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@RunWith(MockitoJUnitRunner.class)
//@FixMethodOrder(MethodSorters.JVM)
//public class CargoServiceImplUpdateTest {
//
//    private CargoMapper cargoMapper = new CargoMapperImpl();
//    private CargoDto updatingCargo;
//
//    @InjectMocks
//    private CargoService cargoService = new CargoServiceImpl(cargoMapper);
//
//    @Mock
//    private CargoRepository cargoRepository;
//
//    @Mock
//    private DriverService driverService;
//
//    @Mock
//    private TruckService truckService;
//
//    @Before
//    public void setUp() {
//        updatingCargo = new CargoDto();
//        updatingCargo.setTitle("Water");
//        updatingCargo.setWeight(300);
//        TruckDto truck = new TruckDto();
//        truck.setId(2L);
//        DriverDto driver = new DriverDto();
//        driver.setId(4L);
//        DriverDto coDriver = new DriverDto();
//        coDriver.setId(5L);
//        updatingCargo.setTruck(truck);
//        updatingCargo.setDriver(driver);
//        updatingCargo.setCoDriver(coDriver);
//    }
//
//    @Test
//    public void updateCargoSuccessfully() {
//        CargoDto existCargo = new CargoDto();
//        existCargo.setId(updatingCargo.getId());
//        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);
//
//        DriverDto existFirstDriver = new DriverDto();
//        existFirstDriver.setId(updatingCargo.getDriver().getId());
//        existFirstDriver.setStatus(DriverStatus.REST);
//
//        DriverDto existCoDriver = new DriverDto();
//        existCoDriver.setId(updatingCargo.getCoDriver().getId());
//        existCoDriver.setStatus(DriverStatus.REST);
//
//        TruckDto existTruck = new TruckDto();
//        existTruck.setId(updatingCargo.getTruck().getId());
//        existTruck.setCondition(TruckCondition.SERVICEABLE);
//        existTruck.setCapacity(700);
//
//        Mockito
//                .when(cargoRepository.getCargoToUpdate(updatingCargo.getId()))
//                .thenReturn(cargoMapper.fromDto(existCargo));
//        Mockito
//                .when(driverService.getFreeDriver(updatingCargo.getDriver().getId()))
//                .thenReturn(existFirstDriver);
//        Mockito
//                .when(driverService.getFreeDriver(updatingCargo.getCoDriver().getId()))
//                .thenReturn(existCoDriver);
//        Mockito
//                .when(truckService.getFreeTruck(updatingCargo.getTruck().getId(), updatingCargo.getWeight()))
//                .thenReturn(existTruck);
//
//        boolean result = cargoService.updateCargo(updatingCargo);
//
//        assertEquals(updatingCargo.getDriver(), existFirstDriver);
//        assertEquals(updatingCargo.getCoDriver(), existCoDriver);
//        assertEquals(updatingCargo.getTruck(), existTruck);
//        assertTrue(result);
//    }
//
//    @Test
//    public void failedUpdateWrongCargoStatus() {
//        CargoDto existCargo = new CargoDto();
//        existCargo.setId(updatingCargo.getId());
//        existCargo.setStatus(CargoStatus.DELIVERED);
//
//        Mockito
//                .when(cargoRepository.getCargoToUpdate(updatingCargo.getId()))
//                .thenReturn(null);
//
//        SavingCargoException thrown = assertThrows(SavingCargoException.class,
//                () -> cargoService.updateCargo(updatingCargo));
//
//        assertTrue(thrown.getMessage().contains("Wrong cargo id or cargo status not equals 'REFUSED_BY_DRIVER'"));
//    }
//
//    @Test
//    public void failedUpdateEqualsDriverIds() {
//        updatingCargo.getDriver().setId(updatingCargo.getCoDriver().getId());
//
//        CargoDto existCargo = new CargoDto();
//        existCargo.setId(updatingCargo.getId());
//        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);
//
//        Mockito
//                .when(cargoRepository.getCargoToUpdate(updatingCargo.getId()))
//                .thenReturn(cargoMapper.fromDto(existCargo));
//
//        SavingCargoException thrown = assertThrows(SavingCargoException.class,
//                () -> cargoService.updateCargo(updatingCargo));
//
//        assertTrue(thrown.getMessage().contains("Driver id and co-driver id cannot be equals"));
//    }
//
//    @Test
//    public void failedUpdateWrongFirstDriverStatus() {
//        updatingCargo.getDriver().setId(4L);
//
//        CargoDto existCargo = new CargoDto();
//        existCargo.setId(updatingCargo.getId());
//        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);
//
//        DriverDto existFirstDriver = new DriverDto();
//        existFirstDriver.setId(updatingCargo.getDriver().getId());
//        existFirstDriver.setStatus(DriverStatus.ACTIVE);
//
//        Mockito
//                .when(cargoRepository.getCargoToUpdate(updatingCargo.getId()))
//                .thenReturn(cargoMapper.fromDto(existCargo));
//        Mockito
//                .when(driverService.getFreeDriver(updatingCargo.getDriver().getId()))
//                .thenReturn(null);
//
//        SavingCargoException thrown = assertThrows(SavingCargoException.class,
//                () -> cargoService.updateCargo(updatingCargo));
//
//        assertTrue(thrown.getMessage().contains("Wrong driver id or driver status"));
//    }
//
//    @Test
//    public void failedUpdateWrongCoDriverStatus() {
//        updatingCargo.getDriver().setId(4L);
//
//        CargoDto existCargo = new CargoDto();
//        existCargo.setId(updatingCargo.getId());
//        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);
//
//        DriverDto existFirstDriver = new DriverDto();
//        existFirstDriver.setId(updatingCargo.getDriver().getId());
//        existFirstDriver.setStatus(DriverStatus.REST);
//
//        DriverDto existCoDriver = new DriverDto();
//        existCoDriver.setId(updatingCargo.getCoDriver().getId());
//        existCoDriver.setStatus(DriverStatus.ACTIVE);
//
//        Mockito
//                .when(cargoRepository.getCargoToUpdate(updatingCargo.getId()))
//                .thenReturn(cargoMapper.fromDto(existCargo));
//        Mockito
//                .when(driverService.getFreeDriver(updatingCargo.getDriver().getId()))
//                .thenReturn(existFirstDriver);
//        Mockito
//                .when(driverService.getFreeDriver(updatingCargo.getCoDriver().getId()))
//                .thenReturn(null);
//
//        SavingCargoException thrown = assertThrows(SavingCargoException.class,
//                () -> cargoService.updateCargo(updatingCargo));
//
//        assertTrue(thrown.getMessage().contains("Wrong co-driver id or co-driver status"));
//    }
//
//    @Test
//    public void failedUpdateWrongTruckStatus() {
//        CargoDto existCargo = new CargoDto();
//        existCargo.setId(updatingCargo.getId());
//        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);
//
//        DriverDto existFirstDriver = new DriverDto();
//        existFirstDriver.setId(updatingCargo.getDriver().getId());
//        existFirstDriver.setStatus(DriverStatus.REST);
//
//        DriverDto existCoDriver = new DriverDto();
//        existCoDriver.setId(updatingCargo.getCoDriver().getId());
//        existCoDriver.setStatus(DriverStatus.REST);
//
//        TruckDto existTruck = new TruckDto();
//        existTruck.setId(updatingCargo.getTruck().getId());
//        existTruck.setCondition(TruckCondition.FAULTY);
//        existTruck.setCapacity(700);
//
//        Mockito
//                .when(cargoRepository.getCargoToUpdate(updatingCargo.getId()))
//                .thenReturn(cargoMapper.fromDto(existCargo));
//        Mockito
//                .when(driverService.getFreeDriver(updatingCargo.getDriver().getId()))
//                .thenReturn(existFirstDriver);
//        Mockito
//                .when(driverService.getFreeDriver(updatingCargo.getCoDriver().getId()))
//                .thenReturn(existCoDriver);
//        Mockito
//                .when(truckService.getFreeTruck(updatingCargo.getTruck().getId(), updatingCargo.getWeight()))
//                .thenReturn(null);
//
//        SavingCargoException thrown = assertThrows(SavingCargoException.class,
//                () -> cargoService.updateCargo(updatingCargo));
//
//        assertTrue(thrown.getMessage().contains("Wrong truck id or truck condition or truck already include in another cargo"));
//    }
//
//    @Test
//    public void failedUpdateWrongTruckCapacity() {
//        updatingCargo.setWeight(1000);
//
//        CargoDto existCargo = new CargoDto();
//        existCargo.setId(updatingCargo.getId());
//        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);
//
//        DriverDto existFirstDriver = new DriverDto();
//        existFirstDriver.setId(updatingCargo.getDriver().getId());
//        existFirstDriver.setStatus(DriverStatus.REST);
//
//        DriverDto existCoDriver = new DriverDto();
//        existCoDriver.setId(updatingCargo.getCoDriver().getId());
//        existCoDriver.setStatus(DriverStatus.REST);
//
//        TruckDto existTruck = new TruckDto();
//        existTruck.setId(updatingCargo.getTruck().getId());
//        existTruck.setCondition(TruckCondition.SERVICEABLE);
//        existTruck.setCapacity(400);
//
//        Mockito
//                .when(cargoRepository.getCargoToUpdate(updatingCargo.getId()))
//                .thenReturn(cargoMapper.fromDto(existCargo));
//        Mockito
//                .when(driverService.getFreeDriver(updatingCargo.getDriver().getId()))
//                .thenReturn(existFirstDriver);
//        Mockito
//                .when(driverService.getFreeDriver(updatingCargo.getCoDriver().getId()))
//                .thenReturn(existCoDriver);
//        Mockito
//                .when(truckService.getFreeTruck(updatingCargo.getTruck().getId(), updatingCargo.getWeight()))
//                .thenReturn(null);
//
//        SavingCargoException thrown = assertThrows(SavingCargoException.class,
//                () -> cargoService.updateCargo(updatingCargo));
//
//        assertTrue(thrown.getMessage().contains("Wrong truck id or truck condition or truck already include in another cargo"));
//    }
//
//    @Test
//    public void failedUpdateTruckInOtherCargo() {
//        CargoDto existCargo = new CargoDto();
//        existCargo.setId(updatingCargo.getId());
//        existCargo.setStatus(CargoStatus.REFUSED_BY_DRIVER);
//
//        DriverDto existFirstDriver = new DriverDto();
//        existFirstDriver.setId(updatingCargo.getDriver().getId());
//        existFirstDriver.setStatus(DriverStatus.REST);
//
//        DriverDto existCoDriver = new DriverDto();
//        existCoDriver.setId(updatingCargo.getCoDriver().getId());
//        existCoDriver.setStatus(DriverStatus.REST);
//
//        TruckDto existTruck = new TruckDto();
//        existTruck.setId(updatingCargo.getTruck().getId());
//        existTruck.setCondition(TruckCondition.SERVICEABLE);
//        existTruck.setCapacity(1200);
//
//        CargoDto anotherCargo = new CargoDto();
//        anotherCargo.setId(8L);
//        anotherCargo.setTruck(existTruck);
//
//        Mockito
//                .when(cargoRepository.getCargoToUpdate(updatingCargo.getId()))
//                .thenReturn(cargoMapper.fromDto(existCargo));
//        Mockito
//                .when(driverService.getFreeDriver(updatingCargo.getDriver().getId()))
//                .thenReturn(existFirstDriver);
//        Mockito
//                .when(driverService.getFreeDriver(updatingCargo.getCoDriver().getId()))
//                .thenReturn(existCoDriver);
//        Mockito
//                .when(truckService.getFreeTruck(updatingCargo.getTruck().getId(), updatingCargo.getWeight()))
//                .thenReturn(null);
//
//        SavingCargoException thrown = assertThrows(SavingCargoException.class,
//                () -> cargoService.updateCargo(updatingCargo));
//
//        assertTrue(thrown.getMessage().contains("Wrong truck id or truck condition or truck already include in another cargo"));
//    }
//}
