package com.example.services.serviceImpl;

import com.example.controller.exceptions.OrderNotFoundException;
import com.example.controller.exceptions.ChangeOrderStatusException;
import com.example.controller.exceptions.SavingOrderException;
import com.example.database.models.Order;
import com.example.database.models.commons.CargoStatus;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.OrderStatus;
import com.example.database.repositories.OrderRepository;
import com.example.services.CityService;
import com.example.services.mappers.OrderMapper;
import com.example.services.models.*;
import com.example.services.OrderService;
import com.example.services.DriverService;
import com.example.services.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Validated
public class OrderServiceImpl implements OrderService {

    private OrderMapper orderMapper;
    private OrderRepository orderRepository;
    private DriverService driverService;
    private TruckService truckService;
    private CityService cityService;

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper, OrderRepository orderRepository,
                            DriverService driverService, TruckService truckService,
                            CityService cityService) {
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
        this.driverService = driverService;
        this.truckService = truckService;
        this.cityService = cityService;
    }

    @Override
    public OrderDto findById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent()) {
            return orderMapper.toDto(order.get());
        } else {
            throw new OrderNotFoundException("Cargo with id: " + orderId + " not found");
        }
    }

    @Override
    public List<OrderDto> getOrders(String text, int page, int pageSize) {
        Pageable request = PageRequest.of(page - 1, pageSize);

        return orderMapper.toListDto(orderRepository.getOrders(text, request));
    }

    @Override
    public boolean updateOrder(@Valid OrderDto orderDto) {
        checkSavingOrder(orderDto, true);

        orderDto.setStatus(OrderStatus.CREATED);
        orderDto.combineSearchString();
        orderRepository.save(orderMapper.fromDto(orderDto));
        return true;
    }

    // save id into search string
    @Override
    public boolean addOrder(@Valid OrderDto order) {
        checkSavingOrder(order, false);

        order.setId(null);
        order.setStatus(OrderStatus.CREATED);
        order.getCargoList().forEach(cargo -> cargo.setStatus(CargoStatus.CREATED));
        order.getCargoList().forEach(cargo -> cargo.setId(null));
        Order savedOrder = orderRepository.save(orderMapper.fromDto(order));
        OrderDto orderDto = orderMapper.toDto(savedOrder);
        orderDto.combineSearchString();

        orderRepository.setOrderSearchString(orderDto.getSearchString(), savedOrder.getId());
        return true;
    }

    @Override
    public OrderDto getOrderByDriverId(Long driverId) {
        OrderDto orderDto = orderMapper.toDto(orderRepository.getOrderByDriverId(driverId));

        if (orderDto == null) {
            throw new OrderNotFoundException("Cargo with driver id: " + driverId + " not found");
        }

        return orderDto;
    }

    @Override
    public boolean setAcceptStatus(Long orderId, Long driverId) {
        Order order = getCheckedOrderToChangeStatus(orderId, driverId);

        if (!order.getStatus().equals(OrderStatus.CREATED)) {
            throw new ChangeOrderStatusException("Attempt to set ACCEPT status to wrong order");
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
            throw new ChangeOrderStatusException("Attempt to set REFUSED_BY_DRIVER status to wrong order");
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
            throw new ChangeOrderStatusException("Wrong order id or order status");
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
            throw new ChangeOrderStatusException("Wrong order id or main driver id");
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
        checkCargoList(savingOrder);
        checkCities(savingOrder);
    }

    private void checkOrder(OrderDto savingOrder) {
        Order order = orderRepository.getOrderToUpdate(savingOrder.getId());

        if (order == null) {
            throw new SavingOrderException("Wrong order id or order status not equals 'REFUSED_BY_DRIVER'");
        }

        savingOrder.setId(order.getId());
    }

    private void checkDriversIds(OrderDto savingOrder) {
        StringBuilder exception = new StringBuilder();

        if (savingOrder.getDriver().getId() == null || savingOrder.getCoDriver().getId() == null) {
            throw new SavingOrderException("Driver id and co-driver id cannot equals null");
        }
        if (savingOrder.getCoDriver().getId().equals(savingOrder.getDriver().getId())) {
            exception.append("Driver id and co-driver id cannot be equals. Driver id: ");
            exception.append(savingOrder.getDriver().getId());
            exception.append(", co-driver id: ");
            exception.append(savingOrder.getCoDriver().getId());
            throw new SavingOrderException(exception.toString());
        }
    }

    private void checkDrivers(OrderDto savingOrder) {
        SimpleDriverDto driver = driverService.getFreeDriver(savingOrder.getDriver().getId());
        if (driver == null) {
            throw new SavingOrderException("Wrong driver id or driver status");
        }

        SimpleDriverDto coDriver = driverService.getFreeDriver(savingOrder.getCoDriver().getId());
        if (coDriver == null) {
            throw new SavingOrderException("Wrong co-driver id or co-driver status");
        }

        savingOrder.setDriver(driver);
        savingOrder.setCoDriver(coDriver);
    }

    private void checkTruck(OrderDto savingOrder) {
        TruckDto truckDto = truckService.getFreeTruck(savingOrder.getTruck().getId(), savingOrder.getId(),
                savingOrder.getTotalWeight());

        if (truckDto == null) {
            throw new SavingOrderException(
                    "Wrong truck id or truck condition or truck already include in another cargo");
        }

        savingOrder.setTruck(truckDto);
    }

    private void checkCargoList(OrderDto savingOrder) {
        double countedTotalWeight = savingOrder.getCargoList().stream()
                .mapToDouble(CargoDto::getWeight)
                .sum();

        if (countedTotalWeight != savingOrder.getTotalWeight()) {
            throw new SavingOrderException("Incorrect total weight");
        }
    }

    private void checkCities(OrderDto savingOrder) {
        boolean isSameLoadAndDischargeLocation = savingOrder.getCargoList().stream()
                .anyMatch(cargoDto ->
                        cargoDto.getLoadLocation().getId().equals(cargoDto.getDischargeLocation().getId()));

        if (isSameLoadAndDischargeLocation) {
            throw new SavingOrderException("Cargo can't has equals load and discharge location");
        }

        Long[] cityIds = Stream.concat(
                savingOrder.getCargoList().stream().map(cargoDto -> cargoDto.getLoadLocation().getId()),
                savingOrder.getCargoList().stream().map(cargoDto -> cargoDto.getDischargeLocation().getId()))
                .distinct()
                .toArray(Long[]::new);

        List<CityDto> cities = cityService.findCitiesByListId(cityIds);

        if (cities.size() != cityIds.length) {
            throw new SavingOrderException("Wrong city id");
        }

        for (CityDto city : cities) {
            for (CargoDto cargoDto : savingOrder.getCargoList()) {
                if (city.getId().equals(cargoDto.getLoadLocation().getId())) {
                    cargoDto.setLoadLocation(city);
                }

                if (city.getId().equals(cargoDto.getDischargeLocation().getId())) {
                    cargoDto.setDischargeLocation(city);
                }
            }
        }
    }
}
