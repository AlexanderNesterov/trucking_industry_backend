package com.example.services.serviceImpl;

import com.example.controller.exceptions.SavingOrderException;
import com.example.database.models.commons.OrderStatus;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.TruckCondition;
import com.example.database.repositories.OrderRepository;
import com.example.services.OrderService;
import com.example.services.mappers.OrderMapper;
import com.example.services.mappers.OrderMapperImpl;
import com.example.services.models.OrderDto;
import com.example.services.models.SimpleDriverDto;
import com.example.services.models.TruckDto;
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


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.JVM)
public class OrderServiceImplUpdateTest {

    private OrderMapper orderMapper = new OrderMapperImpl();
    private OrderDto updatingOrder;

    @InjectMocks
    private OrderService orderService = new OrderServiceImpl(orderMapper);

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DriverService driverService;

    @Mock
    private TruckService truckService;

    @Before
    public void setUp() {
        updatingOrder = new OrderDto();
        TruckDto truck = new TruckDto();
        truck.setId(2L);
        SimpleDriverDto driver = new SimpleDriverDto();
        driver.setId(4L);
        SimpleDriverDto coDriver = new SimpleDriverDto();
        coDriver.setId(5L);
        updatingOrder.setTruck(truck);
        updatingOrder.setDriver(driver);
        updatingOrder.setCoDriver(coDriver);
    }

    @Test
    public void updateOrderSuccessfully() {
        OrderDto existOrder = new OrderDto();
        existOrder.setId(updatingOrder.getId());
        existOrder.setStatus(OrderStatus.REFUSED_BY_DRIVER);

        SimpleDriverDto existFirstDriver = new SimpleDriverDto();
        existFirstDriver.setId(updatingOrder.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        SimpleDriverDto existCoDriver = new SimpleDriverDto();
        existCoDriver.setId(updatingOrder.getCoDriver().getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(updatingOrder.getTruck().getId());
        existTruck.setCondition(TruckCondition.SERVICEABLE);
        existTruck.setCapacity(700);

        Mockito
                .when(orderRepository.getOrderToUpdate(updatingOrder.getId()))
                .thenReturn(orderMapper.fromDto(existOrder));
        Mockito
                .when(driverService.getFreeDriver(updatingOrder.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.getFreeDriver(updatingOrder.getCoDriver().getId()))
                .thenReturn(existCoDriver);
/*        Mockito
                .when(truckService.getFreeTruck(updatingOrder.getTruck().getId(), updatingOrder.getTotalWeight()))
                .thenReturn(existTruck);*/

        boolean result = orderService.updateOrder(updatingOrder);

        assertEquals(updatingOrder.getDriver(), existFirstDriver);
        assertEquals(updatingOrder.getCoDriver(), existCoDriver);
        assertEquals(updatingOrder.getTruck(), existTruck);
        assertTrue(result);
    }

    @Test
    public void failedUpdateWrongOrderStatus() {
        OrderDto existOrder = new OrderDto();
        existOrder.setId(updatingOrder.getId());
        existOrder.setStatus(OrderStatus.DELIVERED);

        Mockito
                .when(orderRepository.getOrderToUpdate(updatingOrder.getId()))
                .thenReturn(null);

        SavingOrderException thrown = assertThrows(SavingOrderException.class,
                () -> orderService.updateOrder(updatingOrder));

        assertTrue(thrown.getMessage().contains("Wrong order id or order status not equals 'REFUSED_BY_DRIVER'"));
    }

    @Test
    public void failedUpdateEqualsDriverIds() {
        updatingOrder.getDriver().setId(updatingOrder.getCoDriver().getId());

        OrderDto existOrder = new OrderDto();
        existOrder.setId(updatingOrder.getId());
        existOrder.setStatus(OrderStatus.REFUSED_BY_DRIVER);

        Mockito
                .when(orderRepository.getOrderToUpdate(updatingOrder.getId()))
                .thenReturn(orderMapper.fromDto(existOrder));

        SavingOrderException thrown = assertThrows(SavingOrderException.class,
                () -> orderService.updateOrder(updatingOrder));

        assertTrue(thrown.getMessage().contains("Driver id and co-driver id cannot be equals"));
    }

    @Test
    public void failedUpdateWrongFirstDriverStatus() {
        updatingOrder.getDriver().setId(4L);

        OrderDto existOrder = new OrderDto();
        existOrder.setId(updatingOrder.getId());
        existOrder.setStatus(OrderStatus.REFUSED_BY_DRIVER);

        SimpleDriverDto existFirstDriver = new SimpleDriverDto();
        existFirstDriver.setId(updatingOrder.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.ACTIVE);

        Mockito
                .when(orderRepository.getOrderToUpdate(updatingOrder.getId()))
                .thenReturn(orderMapper.fromDto(existOrder));
        Mockito
                .when(driverService.getFreeDriver(updatingOrder.getDriver().getId()))
                .thenReturn(null);

        SavingOrderException thrown = assertThrows(SavingOrderException.class,
                () -> orderService.updateOrder(updatingOrder));

        assertTrue(thrown.getMessage().contains("Wrong driver id or driver status"));
    }

    @Test
    public void failedUpdateWrongCoDriverStatus() {
        updatingOrder.getDriver().setId(4L);

        OrderDto existOrder = new OrderDto();
        existOrder.setId(updatingOrder.getId());
        existOrder.setStatus(OrderStatus.REFUSED_BY_DRIVER);

        SimpleDriverDto existFirstDriver = new SimpleDriverDto();
        existFirstDriver.setId(updatingOrder.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        SimpleDriverDto existCoDriver = new SimpleDriverDto();
        existCoDriver.setId(updatingOrder.getCoDriver().getId());
        existCoDriver.setStatus(DriverStatus.ACTIVE);

        Mockito
                .when(orderRepository.getOrderToUpdate(updatingOrder.getId()))
                .thenReturn(orderMapper.fromDto(existOrder));
        Mockito
                .when(driverService.getFreeDriver(updatingOrder.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.getFreeDriver(updatingOrder.getCoDriver().getId()))
                .thenReturn(null);

        SavingOrderException thrown = assertThrows(SavingOrderException.class,
                () -> orderService.updateOrder(updatingOrder));

        assertTrue(thrown.getMessage().contains("Wrong co-driver id or co-driver status"));
    }

    @Test
    public void failedUpdateWrongTruckStatus() {
        OrderDto existOrder = new OrderDto();
        existOrder.setId(updatingOrder.getId());
        existOrder.setStatus(OrderStatus.REFUSED_BY_DRIVER);

        SimpleDriverDto existFirstDriver = new SimpleDriverDto();
        existFirstDriver.setId(updatingOrder.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        SimpleDriverDto existCoDriver = new SimpleDriverDto();
        existCoDriver.setId(updatingOrder.getCoDriver().getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(updatingOrder.getTruck().getId());
        existTruck.setCondition(TruckCondition.FAULTY);
        existTruck.setCapacity(700);

        Mockito
                .when(orderRepository.getOrderToUpdate(updatingOrder.getId()))
                .thenReturn(orderMapper.fromDto(existOrder));
        Mockito
                .when(driverService.getFreeDriver(updatingOrder.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.getFreeDriver(updatingOrder.getCoDriver().getId()))
                .thenReturn(existCoDriver);
/*        Mockito
                .when(truckService.getFreeTruck(updatingOrder.getTruck().getId(), updatingOrder.getTotalWeight()))
                .thenReturn(null);*/

        SavingOrderException thrown = assertThrows(SavingOrderException.class,
                () -> orderService.updateOrder(updatingOrder));

        assertTrue(thrown.getMessage().contains("Wrong truck id or truck condition or truck already include in another cargo"));
    }

    @Test
    public void failedUpdateWrongTruckCapacity() {
        updatingOrder.setTotalWeight(1000);

        OrderDto existOrder = new OrderDto();
        existOrder.setId(updatingOrder.getId());
        existOrder.setStatus(OrderStatus.REFUSED_BY_DRIVER);

        SimpleDriverDto existFirstDriver = new SimpleDriverDto();
        existFirstDriver.setId(updatingOrder.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        SimpleDriverDto existCoDriver = new SimpleDriverDto();
        existCoDriver.setId(updatingOrder.getCoDriver().getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(updatingOrder.getTruck().getId());
        existTruck.setCondition(TruckCondition.SERVICEABLE);
        existTruck.setCapacity(400);

        Mockito
                .when(orderRepository.getOrderToUpdate(updatingOrder.getId()))
                .thenReturn(orderMapper.fromDto(existOrder));
        Mockito
                .when(driverService.getFreeDriver(updatingOrder.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.getFreeDriver(updatingOrder.getCoDriver().getId()))
                .thenReturn(existCoDriver);
/*        Mockito
                .when(truckService.getFreeTruck(updatingOrder.getTruck().getId(), updatingOrder.getTotalWeight()))
                .thenReturn(null);*/

        SavingOrderException thrown = assertThrows(SavingOrderException.class,
                () -> orderService.updateOrder(updatingOrder));

        assertTrue(thrown.getMessage().contains("Wrong truck id or truck condition or truck already include in another cargo"));
    }

    @Test
    public void failedUpdateTruckInOtherOrder() {
        OrderDto existOrder = new OrderDto();
        existOrder.setId(updatingOrder.getId());
        existOrder.setStatus(OrderStatus.REFUSED_BY_DRIVER);

        SimpleDriverDto existFirstDriver = new SimpleDriverDto();
        existFirstDriver.setId(updatingOrder.getDriver().getId());
        existFirstDriver.setStatus(DriverStatus.REST);

        SimpleDriverDto existCoDriver = new SimpleDriverDto();
        existCoDriver.setId(updatingOrder.getCoDriver().getId());
        existCoDriver.setStatus(DriverStatus.REST);

        TruckDto existTruck = new TruckDto();
        existTruck.setId(updatingOrder.getTruck().getId());
        existTruck.setCondition(TruckCondition.SERVICEABLE);
        existTruck.setCapacity(1200);

        OrderDto anotherOrder = new OrderDto();
        anotherOrder.setId(8L);
        anotherOrder.setTruck(existTruck);

        Mockito
                .when(orderRepository.getOrderToUpdate(updatingOrder.getId()))
                .thenReturn(orderMapper.fromDto(existOrder));
        Mockito
                .when(driverService.getFreeDriver(updatingOrder.getDriver().getId()))
                .thenReturn(existFirstDriver);
        Mockito
                .when(driverService.getFreeDriver(updatingOrder.getCoDriver().getId()))
                .thenReturn(existCoDriver);
/*        Mockito
                .when(truckService.getFreeTruck(updatingOrder.getTruck().getId(), updatingOrder.getTotalWeight()))
                .thenReturn(null);*/

        SavingOrderException thrown = assertThrows(SavingOrderException.class,
                () -> orderService.updateOrder(updatingOrder));

        assertTrue(thrown.getMessage().contains("Wrong truck id or truck condition or truck already include in another cargo"));
    }
}
