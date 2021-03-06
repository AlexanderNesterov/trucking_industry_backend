package com.example.services.serviceImpl;

import com.example.controller.exceptions.OrderNotFoundException;
import com.example.database.models.commons.CargoStatus;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.OrderStatus;
import com.example.database.models.commons.TruckCondition;
import com.example.database.repositories.OrderRepository;
import com.example.services.CityService;
import com.example.services.DriverService;
import com.example.services.OrderService;
import com.example.services.TruckService;
import com.example.services.mappers.OrderMapper;
import com.example.services.mappers.OrderMapperImpl;
import com.example.services.models.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {

    private OrderMapper orderMapper = new OrderMapperImpl();

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


    private OrderDto orderDto;

    @Before
    public void setUp() {
        SimpleDriverDto driver = new SimpleDriverDto();
        driver.setId(1L);
        driver.setStatus(DriverStatus.ASSIGNED);
        driver.setDriverLicense("1020304050");
        driver.setUser(new SimpleUserDto());

        SimpleDriverDto coDriver = new SimpleDriverDto();
        coDriver.setId(2L);
        coDriver.setStatus(DriverStatus.ASSIGNED);
        coDriver.setDriverLicense("1020304060");
        coDriver.setUser(new SimpleUserDto());

        TruckDto truck = new TruckDto();
        truck.setCondition(TruckCondition.SERVICEABLE);
        truck.setCapacity(1000);
        truck.setId(10L);

        CityDto firstCity = new CityDto();
        firstCity.setId(2L);
        CityDto secondCity = new CityDto();
        secondCity.setId(5L);
        CityDto thirdCity = new CityDto();
        thirdCity.setId(12L);
        CityDto fourthCity = new CityDto();
        fourthCity.setId(1L);

        CargoDto firstCargo = new CargoDto();
        firstCargo.setWeight(200);
        firstCargo.setStatus(CargoStatus.CREATED);
        firstCargo.setLoadLocation(secondCity);
        firstCargo.setDischargeLocation(firstCity);

        CargoDto secondCargo = new CargoDto();
        secondCargo.setWeight(150);
        secondCargo.setStatus(CargoStatus.CREATED);
        secondCargo.setLoadLocation(thirdCity);
        secondCargo.setDischargeLocation(fourthCity);

        CargoDto thirdCargo = new CargoDto();
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
        orderDto.setStatus(OrderStatus.CREATED);
    }

    @Test
    public void findById_SuitableId_Order() {
        Long orderId = 1L;
        orderDto.setId(orderId);

        Mockito
                .when(orderRepository.findById(orderDto.getId()))
                .thenReturn(Optional.of(orderMapper.fromDto(orderDto)));

        OrderDto foundOrder = orderService.findById(orderDto.getId());

        assertEquals(orderDto.getStatus(), foundOrder.getStatus());
        assertEquals(orderDto.getTotalWeight(), foundOrder.getTotalWeight());
    }

    @Test
    public void findOrderById_OrderNotExist_ExceptionThrows() {
        Long orderId = 90L;

        Mockito
                .when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        OrderNotFoundException thrown = assertThrows(OrderNotFoundException.class,
                () -> orderService.findById(orderId));

        assertTrue(thrown.getMessage().contains("Order with id " + orderId + " not found"));
    }

    @Test
    public void getOrderByDriverId_IdExists_Order() {
        Mockito
                .when(orderRepository.getOrderByDriverId(orderDto.getDriver().getId()))
                .thenReturn(Optional.of(orderMapper.fromDto(orderDto)));

        OrderDto foundOrder = orderService.getOrderByDriverId(orderDto.getDriver().getId());

        assertEquals(orderDto.getTotalWeight(), foundOrder.getTotalWeight());
        assertEquals(orderDto.getStatus(), foundOrder.getStatus());
    }

    @Test
    public void getOrderByDriverId_IdNotExist_ExceptionThrows() {
        Long driverId = 12L;

        OrderNotFoundException thrown = assertThrows(OrderNotFoundException.class,
                () -> orderService.getOrderByDriverId(driverId));

        assertTrue(thrown.getMessage().contains("Order with driver id " + driverId + " not found"));
    }

    @Test
    public void setAcceptStatus_SuitableOrder_True() {
        Long orderId = 24L;
        orderDto.setId(orderId);

        Mockito
                .when(orderRepository.getOrderToChangeStatus(orderDto.getId(), orderDto.getDriver().getId()))
                .thenReturn(Optional.of(orderMapper.fromDto(orderDto)));

        boolean result = orderService.setAcceptStatus(orderDto.getId(), orderDto.getDriver().getId());

/*        assertEquals(OrderStatus.IN_PROGRESS, orderDto.getStatus());
        assertEquals(DriverStatus.ACTIVE, orderDto.getDriver().getStatus());
        assertEquals(DriverStatus.ACTIVE, orderDto.getCoDriver().getStatus());
        orderDto.getCargoList().forEach(cargoDto -> assertEquals(CargoStatus.IN_PROGRESS, cargoDto.getStatus()));*/
        assertTrue(result);
    }
}
