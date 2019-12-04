package com.example.services.serviceImpl;

import com.example.controller.exceptions.CargoNotFoundException;
import com.example.controller.exceptions.ChangeCargoStatusException;
import com.example.controller.exceptions.SavingCargoException;
import com.example.database.models.Order;
import com.example.database.models.commons.CargoStatus;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.OrderStatus;
import com.example.database.repositories.OrderRepository;
import com.example.services.mappers.OrderMapper;
import com.example.services.models.SimpleDriverDto;
import com.example.services.models.OrderDto;
import com.example.services.models.TruckDto;
import com.example.services.OrderService;
import com.example.services.DriverService;
import com.example.services.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return orderRepository.findAll().stream()
                .map(cargo -> orderMapper.toDto(cargo))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersBySearch(String text) {
        return orderMapper.toListDto(orderRepository.getOrdersBySearch(text));
    }

    @Override
    public boolean updateOrder(OrderDto orderDto) {
//        CargoValidator.validate(orderDto);
        checkSavingOrder(orderDto, true);

        orderDto.setStatus(OrderStatus.CREATED);
        orderRepository.save(orderMapper.fromDto(orderDto));
        return true;
    }

    // save id into search string
    @Override
    public boolean addOrder(OrderDto order) {
//        CargoValidator.validate(order);
        checkSavingOrder(order, false);

        order.setId(null);
        order.setStatus(OrderStatus.CREATED);
        order.getCargoList().forEach(cargo -> cargo.setStatus(CargoStatus.CREATED));
        Order savedOrder = orderRepository.save(orderMapper.fromDto(order));
        String searchString  = combineSearchString(savedOrder);
        orderRepository.setOrderSearchString(searchString, savedOrder.getId());
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

        order.getCargoList().forEach(cargo -> cargo.setStatus(CargoStatus.IN_PROGRESS));
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
    public void tryToSetDeliverStatus(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isEmpty()) {
            return;
        }

        Order order = orderOptional.get();

        long deliveredCargoCounter = order.getCargoList()
                .stream()
                .filter(cargo -> cargo.getStatus().equals(CargoStatus.DELIVERED))
                .count();

        if (deliveredCargoCounter == order.getCargoList().size()) {
            order.setStatus(OrderStatus.DELIVERED);
            order.getDriver().setStatus(DriverStatus.REST);
            order.getCoDriver().setStatus(DriverStatus.REST);
            orderRepository.save(order);
        }
    }

    @Override
    public boolean setCanceledStatus(Long orderId) {
        Order order = orderRepository.getOrderToCancel(orderId);

        if (order == null) {
            throw new ChangeCargoStatusException("Wrong order id or order status");
        }

        order.getCargoList().forEach(cargo -> cargo.setStatus(CargoStatus.CANCELED));
        order.setStatus(OrderStatus.CANCELED);
        order.getDriver().setStatus(DriverStatus.REST);
        order.getCoDriver().setStatus(DriverStatus.REST);
        orderRepository.save(order);
        return true;
    }

    public Order getCheckedOrderToChangeStatus(Long orderId, Long driverId) {
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
        SimpleDriverDto driver = driverService.getFreeDriver(savingOrder.getDriver().getId());
        if (driver == null) {
            throw new SavingCargoException("Wrong driver id or driver status");
        }

        SimpleDriverDto coDriver = driverService.getFreeDriver(savingOrder.getCoDriver().getId());
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

    private String combineSearchString(Order order) {
        StringBuilder sb = new StringBuilder();

        sb.append(order.getId());
        sb.append(" ");
        sb.append(order.getDriver().getUser().getFirstName());
        sb.append(" ");
        sb.append(order.getDriver().getUser().getLastName());
        sb.append(" ");
        sb.append(order.getCoDriver().getUser().getFirstName());
        sb.append(" ");
        sb.append(order.getCoDriver().getUser().getLastName());
        sb.append(" ");
        sb.append(order.getTruck().getRegistrationNumber());
        sb.append(" ");
        sb.append(order.getTotalWeight());
        sb.append(" ");
        sb.append(order.getStatus());
        sb.append(" ");
        order.getCargoList().forEach(cargoDto -> sb.append(cargoDto.getTitle()).append(" "));
        order.getCargoList().forEach(cargoDto -> sb.append(cargoDto.getLoadLocation().getName()).append(" "));
        order.getCargoList().forEach(cargoDto -> sb.append(cargoDto.getDischargeLocation().getName()).append(" "));

        return sb.toString().toLowerCase();
    }
}
