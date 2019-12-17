package com.example.services.serviceImpl;

import com.example.controller.exceptions.SavingOrderException;
import com.example.database.models.commons.CargoStatus;
import com.example.database.models.commons.OrderStatus;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.TruckCondition;
import com.example.database.repositories.OrderRepository;
import com.example.services.CityService;
import com.example.services.OrderService;
import com.example.services.mappers.OrderMapper;
import com.example.services.mappers.OrderMapperImpl;
import com.example.services.models.*;
import com.example.services.DriverService;
import com.example.services.TruckService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.services.commons.message.OrderExceptionMessage.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.JVM)
public class OrderServiceImplUpdateTest {

    private OrderMapper orderMapper = new OrderMapperImpl();
    private OrderDto orderDto;

    @InjectMocks
    private OrderService orderService = new OrderServiceImpl(orderMapper);

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DriverService driverService;

    @Mock
    private TruckService truckService;

    @Mock
    private CityService cityService;

    @Before
    public void setUp() {
        SimpleUserDto user1 = new SimpleUserDto();
        user1.setFirstName("Vasilii");
        user1.setLastName("Petrov");

        SimpleUserDto user2 = new SimpleUserDto();
        user2.setFirstName("Petr");
        user2.setLastName("Ivanov");

        SimpleDriverDto driver = new SimpleDriverDto();
        driver.setId(4L);
        driver.setStatus(DriverStatus.REST);
        driver.setDriverLicense("1020304050");
        driver.setUser(user1);

        SimpleDriverDto coDriver = new SimpleDriverDto();
        coDriver.setId(5L);
        coDriver.setStatus(DriverStatus.REST);
        coDriver.setDriverLicense("1020304060");
        coDriver.setUser(user2);

        TruckDto truck = new TruckDto();
        truck.setCondition(TruckCondition.SERVICEABLE);
        truck.setCapacity(1000);
        truck.setId(2L);

        CityDto firstCity = new CityDto();
        firstCity.setName("Berlin");
        firstCity.setId(2L);
        CityDto secondCity = new CityDto();
        secondCity.setName("Moscow");
        secondCity.setId(5L);
        CityDto thirdCity = new CityDto();
        thirdCity.setName("Voronezh");
        thirdCity.setId(12L);
        CityDto fourthCity = new CityDto();
        fourthCity.setName("London");
        fourthCity.setId(1L);

        CargoDto firstCargo = new CargoDto();
        firstCargo.setTitle("Water");
        firstCargo.setWeight(200);
        firstCargo.setStatus(CargoStatus.CREATED);
        firstCargo.setLoadLocation(secondCity);
        firstCargo.setDischargeLocation(firstCity);

        CargoDto secondCargo = new CargoDto();
        secondCargo.setTitle("Fuel");
        secondCargo.setWeight(150);
        secondCargo.setStatus(CargoStatus.CREATED);
        secondCargo.setLoadLocation(thirdCity);
        secondCargo.setDischargeLocation(fourthCity);

        CargoDto thirdCargo = new CargoDto();
        thirdCargo.setTitle("Food");
        thirdCargo.setWeight(300);
        thirdCargo.setStatus(CargoStatus.CREATED);
        thirdCargo.setLoadLocation(firstCity);
        thirdCargo.setDischargeLocation(thirdCity);

        orderDto = new OrderDto();
        orderDto.setDriver(driver);
        orderDto.setCoDriver(coDriver);
        orderDto.setTruck(truck);
        orderDto.setCargoList(Arrays.asList(firstCargo, secondCargo, thirdCargo));
        orderDto.setTotalWeight(650);
        orderDto.setStatus(OrderStatus.REFUSED_BY_DRIVER);
    }

    @Test
    public void updateOrderSuccessfully() {
        SimpleUserDto user1 = new SimpleUserDto();
        user1.setFirstName("Vasilii");
        user1.setLastName("Petrov");

        SimpleUserDto user2 = new SimpleUserDto();
        user2.setFirstName("Petr");
        user2.setLastName("Ivanov");

        SimpleDriverDto existFirstDriver = new SimpleDriverDto();
        existFirstDriver.setId(4L);
        existFirstDriver.setStatus(DriverStatus.REST);
        existFirstDriver.setDriverLicense("1020304050");
        existFirstDriver.setUser(user1);

        SimpleDriverDto existCoDriver = new SimpleDriverDto();
        existCoDriver.setId(5L);
        existCoDriver.setStatus(DriverStatus.REST);
        existCoDriver.setDriverLicense("1020304060");
        existCoDriver.setUser(user2);

        OrderDto existOrder = new OrderDto();
        existOrder.setId(orderDto.getId());
        existOrder.setStatus(OrderStatus.REFUSED_BY_DRIVER);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(orderDto.getTruck().getId());
        existTruck.setCondition(TruckCondition.SERVICEABLE);
        existTruck.setCapacity(700);

        List<CityDto> cities = Stream.concat(
                orderDto.getCargoList().stream().map(CargoDto::getLoadLocation),
                orderDto.getCargoList().stream().map(CargoDto::getDischargeLocation))
                .distinct()
                .collect(Collectors.toList());

        Long[] cityIds = Stream.concat(
                orderDto.getCargoList().stream().map(cargoDto -> cargoDto.getLoadLocation().getId()),
                orderDto.getCargoList().stream().map(cargoDto -> cargoDto.getDischargeLocation().getId()))
                .distinct()
                .toArray(Long[]::new);

        Mockito
                .when(orderRepository.getOrderToUpdate(orderDto.getId()))
                .thenReturn(orderMapper.fromDto(existOrder));
        Mockito
                .when(driverService.getFreeDriver(orderDto.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.getFreeDriver(orderDto.getCoDriver().getId()))
                .thenReturn(existCoDriver);
        Mockito
                .when(truckService.getFreeTruck(orderDto.getTruck().getId(), orderDto.getId(),
                        orderDto.getTotalWeight()))
                .thenReturn(existTruck);
        Mockito
                .when(cityService.findCitiesByListId(cityIds))
                .thenReturn(cities);

        boolean result = orderService.updateOrder(orderDto);

        assertEquals(orderDto.getDriver(), existFirstDriver);
        assertEquals(orderDto.getCoDriver(), existCoDriver);
        assertEquals(orderDto.getTruck(), existTruck);
        assertTrue(result);
    }

    @Test
    public void failedUpdateWrongOrderStatus() {
        OrderDto existOrder = new OrderDto();
        existOrder.setId(orderDto.getId());
        existOrder.setStatus(OrderStatus.DELIVERED);

        Mockito
                .when(orderRepository.getOrderToUpdate(orderDto.getId()))
                .thenReturn(null);

        SavingOrderException thrown = assertThrows(SavingOrderException.class,
                () -> orderService.updateOrder(orderDto));

        assertEquals(thrown.getMessage(), WRONG_ORDER);
    }

    @Test
    public void failedUpdateEqualsDriverIds() {
        orderDto.getDriver().setId(orderDto.getCoDriver().getId());

        OrderDto existOrder = new OrderDto();
        existOrder.setId(orderDto.getId());
        existOrder.setStatus(OrderStatus.REFUSED_BY_DRIVER);

        Mockito
                .when(orderRepository.getOrderToUpdate(orderDto.getId()))
                .thenReturn(orderMapper.fromDto(existOrder));

        SavingOrderException thrown = assertThrows(SavingOrderException.class,
                () -> orderService.updateOrder(orderDto));

        assertEquals(thrown.getMessage(),
                String.format(EQUALS_DRIVER_ID, orderDto.getDriver().getId(), orderDto.getCoDriver().getId()));
    }

    @Test
    public void failedUpdateWrongFirstDriverStatus() {
        orderDto.getDriver().setId(4L);

        OrderDto existOrder = new OrderDto();
        existOrder.setId(orderDto.getId());
        existOrder.setStatus(OrderStatus.REFUSED_BY_DRIVER);

        SimpleDriverDto existFirstDriver = new SimpleDriverDto();
        existFirstDriver.setId(orderDto.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.ACTIVE);

        Mockito
                .when(orderRepository.getOrderToUpdate(orderDto.getId()))
                .thenReturn(orderMapper.fromDto(existOrder));
        Mockito
                .when(driverService.getFreeDriver(orderDto.getDriver().getId()))
                .thenReturn(null);

        SavingOrderException thrown = assertThrows(SavingOrderException.class,
                () -> orderService.updateOrder(orderDto));

        assertEquals(thrown.getMessage(), WRONG_DRIVER);
    }

    @Test
    public void failedUpdateWrongCoDriverStatus() {
        orderDto.getDriver().setId(4L);

        OrderDto existOrder = new OrderDto();
        existOrder.setId(orderDto.getId());
        existOrder.setStatus(OrderStatus.REFUSED_BY_DRIVER);

        SimpleDriverDto existFirstDriver = new SimpleDriverDto();
        existFirstDriver.setId(orderDto.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        SimpleDriverDto existCoDriver = new SimpleDriverDto();
        existCoDriver.setId(orderDto.getCoDriver().getId());
        existCoDriver.setStatus(DriverStatus.ACTIVE);

        Mockito
                .when(orderRepository.getOrderToUpdate(orderDto.getId()))
                .thenReturn(orderMapper.fromDto(existOrder));
        Mockito
                .when(driverService.getFreeDriver(orderDto.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.getFreeDriver(orderDto.getCoDriver().getId()))
                .thenReturn(null);

        SavingOrderException thrown = assertThrows(SavingOrderException.class,
                () -> orderService.updateOrder(orderDto));

        assertEquals(thrown.getMessage(), WRONG_CO_DRIVER);
    }

    @Test
    public void failedUpdateWrongTruckStatus() {
        OrderDto existOrder = new OrderDto();
        existOrder.setId(orderDto.getId());
        existOrder.setStatus(OrderStatus.REFUSED_BY_DRIVER);

        SimpleDriverDto existFirstDriver = new SimpleDriverDto();
        existFirstDriver.setId(orderDto.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        SimpleDriverDto existCoDriver = new SimpleDriverDto();
        existCoDriver.setId(orderDto.getCoDriver().getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(orderDto.getTruck().getId());
        existTruck.setCondition(TruckCondition.BROKEN);
        existTruck.setCapacity(700);

        Mockito
                .when(orderRepository.getOrderToUpdate(orderDto.getId()))
                .thenReturn(orderMapper.fromDto(existOrder));
        Mockito
                .when(driverService.getFreeDriver(orderDto.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.getFreeDriver(orderDto.getCoDriver().getId()))
                .thenReturn(existCoDriver);
        Mockito
                .when(truckService.getFreeTruck(orderDto.getTruck().getId(), orderDto.getId(),
                        orderDto.getTotalWeight()))
                .thenReturn(null);

        SavingOrderException thrown = assertThrows(SavingOrderException.class,
                () -> orderService.updateOrder(orderDto));

        assertEquals(thrown.getMessage(), WRONG_TRUCK);
    }

    @Test
    public void failedUpdateWrongTruckCapacity() {
        orderDto.setTotalWeight(1000);

        OrderDto existOrder = new OrderDto();
        existOrder.setId(orderDto.getId());
        existOrder.setStatus(OrderStatus.REFUSED_BY_DRIVER);

        SimpleDriverDto existFirstDriver = new SimpleDriverDto();
        existFirstDriver.setId(orderDto.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        SimpleDriverDto existCoDriver = new SimpleDriverDto();
        existCoDriver.setId(orderDto.getCoDriver().getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(orderDto.getTruck().getId());
        existTruck.setCondition(TruckCondition.SERVICEABLE);
        existTruck.setCapacity(400);

        Mockito
                .when(orderRepository.getOrderToUpdate(orderDto.getId()))
                .thenReturn(orderMapper.fromDto(existOrder));
        Mockito
                .when(driverService.getFreeDriver(orderDto.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.getFreeDriver(orderDto.getCoDriver().getId()))
                .thenReturn(existCoDriver);
        Mockito
                .when(truckService.getFreeTruck(orderDto.getTruck().getId(), orderDto.getId(),
                        orderDto.getTotalWeight()))
                .thenReturn(null);

        SavingOrderException thrown = assertThrows(SavingOrderException.class,
                () -> orderService.updateOrder(orderDto));

        assertTrue(thrown.getMessage().contains(WRONG_TRUCK));
    }

    @Test
    public void failedUpdateTruckInOtherOrder() {
        OrderDto existOrder = new OrderDto();
        existOrder.setId(orderDto.getId());
        existOrder.setStatus(OrderStatus.REFUSED_BY_DRIVER);

        SimpleDriverDto existFirstDriver = new SimpleDriverDto();
        existFirstDriver.setId(orderDto.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        SimpleDriverDto existCoDriver = new SimpleDriverDto();
        existCoDriver.setId(orderDto.getCoDriver().getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(orderDto.getTruck().getId());
        existTruck.setCondition(TruckCondition.SERVICEABLE);
        existTruck.setCapacity(1200);

        OrderDto anotherOrder = new OrderDto();
        anotherOrder.setId(8L);
        anotherOrder.setTruck(existTruck);

        Mockito
                .when(orderRepository.getOrderToUpdate(orderDto.getId()))
                .thenReturn(orderMapper.fromDto(existOrder));
        Mockito
                .when(driverService.getFreeDriver(orderDto.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.getFreeDriver(orderDto.getCoDriver().getId()))
                .thenReturn(existCoDriver);
        Mockito
                .when(truckService.getFreeTruck(orderDto.getTruck().getId(), orderDto.getId(),
                        orderDto.getTotalWeight()))
                .thenReturn(null);

        SavingOrderException thrown = assertThrows(SavingOrderException.class,
                () -> orderService.updateOrder(orderDto));

        assertTrue(thrown.getMessage().contains(WRONG_TRUCK));
    }
}
