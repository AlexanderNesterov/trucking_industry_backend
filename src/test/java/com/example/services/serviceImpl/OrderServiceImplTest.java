package com.example.services.serviceImpl;

import com.example.controller.exceptions.CargoNotFoundException;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.OrderStatus;
import com.example.database.models.commons.TruckCondition;
import com.example.database.repositories.OrderRepository;
import com.example.services.mappers.OrderMapperImpl;
import com.example.services.models.CargoDto;
import com.example.services.models.SimpleDriverDto;
import com.example.services.models.OrderDto;
import com.example.services.models.TruckDto;
import com.example.services.OrderService;
import com.example.services.DriverService;
import com.example.services.TruckService;
import com.example.services.mappers.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
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

    @Test
    public void findByIdSuccessfully() {
        OrderDto existOrder = new OrderDto();
        existOrder.setId(45L);
        existOrder.setStatus(OrderStatus.CREATED);

        Mockito
                .when(orderRepository.findById(45L))
                .thenReturn(Optional.of(orderMapper.fromDto(existOrder)));
        OrderDto foundOrder = orderService.findById(45L);

        assertEquals(existOrder.getStatus(), foundOrder.getStatus());
    }

    @Test
    public void failedFindOrderById() {
        Mockito
                .when(orderRepository.findById(90L))
                .thenReturn(Optional.empty());

        CargoNotFoundException thrown = assertThrows(CargoNotFoundException.class,
                () -> orderService.findById(90L));

        assertTrue(thrown.getMessage().contains("not found"));
    }


    @Test
    public void addCargoSuccessfully() {
        OrderDto savingOrder = new OrderDto();
        SimpleDriverDto firstDriver = new SimpleDriverDto();
        firstDriver.setId(3L);
        SimpleDriverDto coDriver = new SimpleDriverDto();
        coDriver.setId(7L);
        TruckDto truck = new TruckDto();
        truck.setId(2L);
        CargoDto cargoDto = new CargoDto();
        savingOrder.setDriver(firstDriver);
        savingOrder.setCoDriver(coDriver);
        savingOrder.setTruck(truck);
        savingOrder.setCargoList(List.of(cargoDto));

        SimpleDriverDto existFirstDriver = new SimpleDriverDto();
        existFirstDriver.setId(firstDriver.getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        SimpleDriverDto existCoDriver = new SimpleDriverDto();
        existCoDriver.setId(coDriver.getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(truck.getId());
        existTruck.setCondition(TruckCondition.SERVICEABLE);
        existTruck.setCapacity(700);

        Mockito
                .when(driverService.getFreeDriver(savingOrder.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.getFreeDriver(savingOrder.getCoDriver().getId()))
                .thenReturn(existCoDriver);
        Mockito
                .when(truckService.getFreeTruck(savingOrder.getTruck().getId(), savingOrder.getTotalWeight()))
                .thenReturn(existTruck);

        boolean result = orderService.addOrder(savingOrder);

        assertEquals(existFirstDriver, savingOrder.getDriver());
        assertEquals(existCoDriver, savingOrder.getCoDriver());
        assertEquals(existTruck, savingOrder.getTruck());
        assertNull(savingOrder.getId());
        assertEquals(OrderStatus.CREATED, savingOrder.getStatus());
        assertTrue(result);
    }

    @Test
    public void getCargoByDriverIdSuccessfully() {
        OrderDto existOrder = new OrderDto();
        existOrder.setId(5L);
        existOrder.setTotalWeight(220);
        existOrder.setStatus(OrderStatus.IN_PROGRESS);

        Mockito
                .when(orderRepository.findById(5L))
                .thenReturn(Optional.of(orderMapper.fromDto(existOrder)));
        OrderDto foundOrder = orderService.findById(5L);

        assertEquals(existOrder.getTotalWeight(), foundOrder.getTotalWeight());
        assertEquals(existOrder.getStatus(), foundOrder.getStatus());
    }

    @Test
    public void failedGetCargoByDriverId() {
        CargoNotFoundException thrown = assertThrows(CargoNotFoundException.class,
                () -> orderService.getOrderByDriverId(12L));

        assertTrue(thrown.getMessage().contains("not found"));
    }
}
