package com.example.services.serviceImpl;

import com.example.controller.exceptions.CargoNotFoundException;
import com.example.controller.exceptions.ChangeCargoStatusException;
import com.example.controller.exceptions.SavingCargoException;
import com.example.database.models.Cargo;
import com.example.database.models.Order;
import com.example.database.models.commons.CargoStatus;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.OrderStatus;
import com.example.database.repositories.OrderRepository;
import com.example.services.mappers.OrderMapper;
import com.example.services.models.CargoDto;
import com.example.services.models.DriverDto;
import com.example.services.models.OrderDto;
import com.example.services.models.TruckDto;
import com.example.services.OrderService;
import com.example.services.DriverService;
import com.example.services.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderMapper orderMapper;
    private OrderRepository orderRepository;
    private DriverService driverService;
    private TruckService truckService;

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper, OrderRepository orderRepository,
                            DriverService driverService, TruckService truckService) {
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.driverService = driverService;
        this.truckService = truckService;
    }

    @Override
    public OrderDto findById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            return orderMapper.toDto(order.get());
        } else {
            throw new CargoNotFoundException("Cargo with id: " + orderId + " not found");
        }
    }

    @Override
    public List<OrderDto> findAll() {
        List<OrderDto> orderDtoList = new ArrayList<>();
        orderRepository.findAll().forEach(cargo -> orderDtoList.add(orderMapper.toDto(cargo)));

        return orderDtoList;
    }

    @Override
    public boolean updateOrder(OrderDto orderDto) {
//        CargoValidator.validate(orderDto);
        checkSavingOrder(orderDto, true);

        orderDto.setStatus(OrderStatus.CREATED);
        orderRepository.save(orderMapper.fromDto(orderDto));
        return true;
    }

    @Override
    public boolean addOrder(OrderDto orderDto) {
//        CargoValidator.validate(orderDto);
        checkSavingOrder(orderDto, false);

        orderDto.setId(null);
        orderDto.setStatus(OrderStatus.CREATED);

        for (CargoDto cargo : orderDto.getCargoList()) {
            cargo.setStatus(CargoStatus.CREATED);
        }

        orderRepository.save(orderMapper.fromDto(orderDto));
        return true;
    }

    @Override
    public OrderDto getOrderByDriverId(Long driverId) {
        OrderDto orderDto = orderMapper.toDto(orderRepository.getOrderByDriverId(driverId));

        if (orderDto == null) {
            throw new CargoNotFoundException("Cargo with driver id: " + driverId + " not found");
        }

        return orderDto;
    }

    @Override
    public boolean setAcceptStatus(Long orderId, Long driverId) {
        Order order = getCheckedOrderToChangeStatus(orderId, driverId);

        if (!order.getStatus().equals(OrderStatus.CREATED)) {
            throw new ChangeCargoStatusException("Attempt to set ACCEPT status to wrong order");
        }

        order.setStatus(OrderStatus.IN_PROGRESS);
        order.getDriver().setStatus(DriverStatus.ACTIVE);
        order.getCoDriver().setStatus(DriverStatus.ACTIVE);
        orderRepository.save(order);

        return true;
    }

    @Override
    public boolean setRefuseStatus(Long orderId, Long driverId) {
        Order order = getCheckedOrderToChangeStatus(orderId, driverId);

        if (!order.getStatus().equals(OrderStatus.CREATED)) {
            throw new ChangeCargoStatusException("Attempt to set REFUSED_BY_DRIVER status to wrong order");
        }

        order.setStatus(OrderStatus.REFUSED_BY_DRIVER);
        order.getDriver().setStatus(DriverStatus.REST);
        order.getCoDriver().setStatus(DriverStatus.REST);
        orderRepository.save(order);

        return true;
    }

    @Override
    public boolean setDeliverStatus(Long orderId, Long driverId) {
        Order order = getCheckedOrderToChangeStatus(orderId, driverId);

        if (!order.getStatus().equals(OrderStatus.IN_PROGRESS)) {
            throw new ChangeCargoStatusException("Attempt to set IN_PROGRESS status to wrong order");
        }

        order.setStatus(OrderStatus.DELIVERED);
        order.getDriver().setStatus(DriverStatus.REST);
        order.getCoDriver().setStatus(DriverStatus.REST);
        orderRepository.save(order);

        return true;
    }

    @Override
    public boolean setCanceledStatus(Long orderId) {
        Order order = orderRepository.getOrderToCancel(orderId);

        if (order == null) {
            throw new ChangeCargoStatusException("Wrong order id or order status");
        }

        order.getDriver().setStatus(DriverStatus.REST);
        order.getCoDriver().setStatus(DriverStatus.REST);
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        return true;
    }

    private Order getCheckedOrderToChangeStatus(Long orderId, Long driverId) {
        Order order = orderRepository.getOrderToChangeStatus(orderId, driverId);

        if (order == null) {
            throw new ChangeCargoStatusException("Wrong order id or main driver id");
        }

        return order;
    }

    private void checkSavingOrder(OrderDto savingOrder, boolean isUpdate) {
        if (isUpdate) {
            checkOrder(savingOrder);
        }

        checkDriversIds(savingOrder);
        checkDrivers(savingOrder);
        checkTruck(savingOrder);
    }

    private void checkOrder(OrderDto savingOrder) {
        Order order = orderRepository.getOrderToUpdate(savingOrder.getId());

        if (order == null) {
            throw new SavingCargoException("Wrong order id or order status not equals 'REFUSED_BY_DRIVER'");
        }
    }

    private void checkDriversIds(OrderDto savingOrder) {
        StringBuilder exception = new StringBuilder();

        if (savingOrder.getDriver().getId() == null || savingOrder.getCoDriver().getId() == null) {
            throw new SavingCargoException("Driver id and co-driver id cannot equals null");
        }
        if (savingOrder.getCoDriver().getId().equals(savingOrder.getDriver().getId())) {
            exception.append("Driver id and co-driver id cannot be equals. Driver id: ");
            exception.append(savingOrder.getDriver().getId());
            exception.append(", co-driver id: ");
            exception.append(savingOrder.getCoDriver().getId());
            throw new SavingCargoException(exception.toString());
        }
    }

    private void checkDrivers(OrderDto savingOrder) {
        DriverDto driver = driverService.getFreeDriver(savingOrder.getDriver().getId());
        if (driver == null) {
            throw new SavingCargoException("Wrong driver id or driver status");
        }

        DriverDto coDriver = driverService.getFreeDriver(savingOrder.getCoDriver().getId());
        if (coDriver == null) {
            throw new SavingCargoException("Wrong co-driver id or co-driver status");
        }

        savingOrder.setDriver(driver);
        savingOrder.setCoDriver(coDriver);
    }

    private void checkTruck(OrderDto savingOrder) {
        TruckDto truckDto = truckService.getFreeTruck(savingOrder.getTruck().getId(), savingOrder.getTotalWeight());

        if (truckDto == null) {
            throw new SavingCargoException(
                    "Wrong truck id or truck condition or truck already include in another cargo");
        }

        savingOrder.setTruck(truckDto);
    }

    private void checkCargoList(OrderDto savingOrder) {
        List<CargoDto> cargoList = savingOrder.getCargoList();
    }
}
