package com.example.services.serviceImpl;

import com.example.controller.exceptions.ChangeOrderStatusException;
import com.example.controller.exceptions.OrderNotFoundException;
import com.example.controller.exceptions.SavingOrderException;
import com.example.database.models.Order;
import com.example.database.models.commons.CargoStatus;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.OrderStatus;
import com.example.database.repositories.OrderRepository;
import com.example.services.CityService;
import com.example.services.DriverService;
import com.example.services.OrderService;
import com.example.services.TruckService;
import com.example.services.mappers.OrderMapper;
import com.example.services.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.services.commons.message.OrderExceptionMessage.*;

@Service
@Validated
public class OrderServiceImpl implements OrderService {
    private final int cargoLimit = 5;
    private final static Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

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
            LOGGER.info("Order with id: {} returned", orderId);
            return orderMapper.toDto(order.get());
        } else {
            LOGGER.warn(String.format(ORDER_NOT_FOUND, orderId));
            throw new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId));
        }
    }

    @Override
    public List<OrderDto> getOrders(String text, int page, int pageSize) {
        Pageable request = PageRequest.of(page - 1, pageSize, Sort.by("id").ascending());
        return orderMapper.toListDtoWithoutCargoList(orderRepository.getOrders(text, request));
    }

    @Override
    public List<Order> getOrdersToSendMail() {
        return orderRepository.getOrdersToSendMail();
    }

    @Transactional
    @Override
    public boolean updateOrder(@Valid OrderDto orderDto) {
        checkSavingOrder(orderDto, true);

        orderDto.getCargoList().forEach(cargo -> cargo.setStatus(CargoStatus.CREATED));
        orderDto.setStatus(OrderStatus.CREATED);
        orderDto.getDriver().setStatus(DriverStatus.ASSIGNED);
        orderDto.getCoDriver().setStatus(DriverStatus.ASSIGNED);

        Order order = orderMapper.fromDto(orderDto);
        order.getCargoList().forEach(cargo -> cargo.setOrder(order));
        order.combineSearchString();
        orderRepository.save(order);
        LOGGER.info("Order with id: {} updated", orderDto.getId());
        return true;
    }

    @Transactional
    @Override
    public boolean addOrder(@Valid OrderDto order) {
        checkSavingOrder(order, false);

        order.setId(null);
        order.setStatus(OrderStatus.CREATED);
        order.getCargoList().forEach(cargo -> cargo.setStatus(CargoStatus.CREATED));
        order.getCargoList().forEach(cargo -> cargo.setId(null));
        Order savedOrder = orderRepository.save(orderMapper.fromDto(order));
        savedOrder.combineSearchString();
        driverService.setDriverStatus(new Long[]{order.getDriver().getId(), order.getCoDriver().getId()},
                DriverStatus.ASSIGNED);

        orderRepository.setOrderSearchString(savedOrder.getSearchString(), savedOrder.getId());
        LOGGER.info("Order with id: {} added", savedOrder.getId());
        return true;
    }

    @Override
    public OrderDto getOrderByDriverId(Long driverId) {
        Optional<Order> orderOpt = orderRepository.getOrderByDriverId(driverId);

        if (orderOpt.isEmpty()) {
            LOGGER.warn(String.format(ORDER_BY_DRIVER_NOT_FOUND, driverId));
            throw new OrderNotFoundException(String.format(ORDER_BY_DRIVER_NOT_FOUND, driverId));
        } else {
            LOGGER.info("Order with driver id: {} returned", driverId);
            return orderMapper.toDto(orderOpt.get());
        }
    }

    @Override
    public boolean setAcceptStatus(Long orderId, Long driverId) {
        Order order = getCheckedOrderToChangeStatus(orderId, driverId);

        if (!order.getStatus().equals(OrderStatus.CREATED)) {
            LOGGER.warn(String.format(WRONG_ORDER_STATUS, OrderStatus.IN_PROGRESS));
            throw new ChangeOrderStatusException(String.format(WRONG_ORDER_STATUS, OrderStatus.IN_PROGRESS));
        }

        order.getCargoList().forEach(cargo -> cargo.setStatus(CargoStatus.IN_PROGRESS));
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.getDriver().setStatus(DriverStatus.ACTIVE);
        order.getCoDriver().setStatus(DriverStatus.ACTIVE);
        order.combineSearchString();
        orderRepository.save(order);
        LOGGER.info("Order with id: {} accepted", orderId);
        return true;
    }

    @Override
    public boolean setRefuseStatus(Long orderId, Long driverId) {
        Order order = getCheckedOrderToChangeStatus(orderId, driverId);

        if (!order.getStatus().equals(OrderStatus.CREATED)) {
            LOGGER.warn(String.format(WRONG_ORDER_STATUS, OrderStatus.REFUSED_BY_DRIVER));
            throw new ChangeOrderStatusException(String.format(WRONG_ORDER_STATUS, OrderStatus.REFUSED_BY_DRIVER));
        }

        order.setStatus(OrderStatus.REFUSED_BY_DRIVER);
        order.getDriver().setStatus(DriverStatus.REST);
        order.getCoDriver().setStatus(DriverStatus.REST);
        order.combineSearchString();
        orderRepository.save(order);
        LOGGER.info("Order with id: {} refused by driver", orderId);
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
            order.combineSearchString();
            orderRepository.save(order);
            LOGGER.info("Order with id: {} delivered", orderId);
        }
    }

    @Override
    public boolean setCanceledStatus(Long orderId) {
        Order order = orderRepository.getOrderToCancel(orderId);

        if (order == null) {
            LOGGER.warn(WRONG_ORDER);
            throw new ChangeOrderStatusException(WRONG_ORDER);
        }

        order.getCargoList().forEach(cargo -> cargo.setStatus(CargoStatus.CANCELED));
        order.setStatus(OrderStatus.CANCELED);
        order.getDriver().setStatus(DriverStatus.REST);
        order.getCoDriver().setStatus(DriverStatus.REST);
        order.combineSearchString();
        orderRepository.save(order);
        LOGGER.info("Order with id: {} canceled", orderId);
        return true;
    }

    @Override
    public void setEmailSent(Long orderId) {
        orderRepository.setSentEmail(orderId);
    }

    private Order getCheckedOrderToChangeStatus(Long orderId, Long driverId) {
        Optional<Order> order = orderRepository.getOrderToChangeStatus(orderId, driverId);

        if (order.isEmpty()) {
            LOGGER.warn(String.format(WRONG_ORDER_OR_DRIVER, orderId, driverId));
            throw new ChangeOrderStatusException(String.format(WRONG_ORDER_OR_DRIVER, orderId, driverId));
        }

        return order.get();
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
            LOGGER.warn(WRONG_ORDER);
            throw new SavingOrderException(WRONG_ORDER);
        }

        savingOrder.setId(order.getId());
    }

    private void checkDriversIds(OrderDto savingOrder) {
        if (savingOrder.getDriver().getId() == null || savingOrder.getCoDriver().getId() == null) {
            LOGGER.warn(DRIVER_ID_NULL);
            throw new SavingOrderException(DRIVER_ID_NULL);
        }
        if (savingOrder.getCoDriver().getId().equals(savingOrder.getDriver().getId())) {
            LOGGER.warn(String
                    .format(EQUALS_DRIVER_ID, savingOrder.getDriver().getId(), savingOrder.getCoDriver().getId()));
            throw new SavingOrderException(String
                    .format(EQUALS_DRIVER_ID, savingOrder.getDriver().getId(), savingOrder.getCoDriver().getId()));
        }
    }

    private void checkDrivers(OrderDto savingOrder) {
        SimpleDriverDto driver = driverService.getFreeDriver(savingOrder.getDriver().getId());
        if (driver == null) {
            LOGGER.warn(WRONG_ORDER);
            throw new SavingOrderException(WRONG_DRIVER);
        }

        SimpleDriverDto coDriver = driverService.getFreeDriver(savingOrder.getCoDriver().getId());
        if (coDriver == null) {
            LOGGER.warn(WRONG_CO_DRIVER);
            throw new SavingOrderException(WRONG_CO_DRIVER);
        }

        savingOrder.setDriver(driver);
        savingOrder.setCoDriver(coDriver);
    }

    private void checkTruck(OrderDto savingOrder) {
        TruckDto truckDto = truckService.getFreeTruck(savingOrder.getTruck().getId(), savingOrder.getId(),
                savingOrder.getTotalWeight());

        if (truckDto == null) {
            LOGGER.warn(WRONG_TRUCK);
            throw new SavingOrderException(WRONG_TRUCK);
        }

        savingOrder.setTruck(truckDto);
    }

    private void checkCargoList(OrderDto savingOrder) {
        double countedTotalWeight = savingOrder.getCargoList().stream()
                .mapToDouble(CargoDto::getWeight)
                .sum();

        if (countedTotalWeight != savingOrder.getTotalWeight()) {
            LOGGER.warn(TOTAL_WEIGHT);
            throw new SavingOrderException(TOTAL_WEIGHT);
        }

        if (savingOrder.getCargoList().size() >= cargoLimit) {
            LOGGER.warn(NUMBER_OF_CARGO);
            throw new SavingOrderException(NUMBER_OF_CARGO);
        }
    }

    private void checkCities(OrderDto savingOrder) {
        boolean isSameLoadAndDischargeLocation = savingOrder.getCargoList().stream()
                .anyMatch(cargoDto ->
                        cargoDto.getLoadLocation().getId().equals(cargoDto.getDischargeLocation().getId()));

        if (isSameLoadAndDischargeLocation) {
            LOGGER.warn(EQUALS_LOCATIONS);
            throw new SavingOrderException(EQUALS_LOCATIONS);
        }

        Long[] cityIds = Stream.concat(
                savingOrder.getCargoList().stream().map(cargoDto -> cargoDto.getLoadLocation().getId()),
                savingOrder.getCargoList().stream().map(cargoDto -> cargoDto.getDischargeLocation().getId()))
                .distinct()
                .toArray(Long[]::new);

        List<CityDto> cities = cityService.findCitiesByListId(cityIds);

        if (cities.size() != cityIds.length) {
            LOGGER.warn(WRONG_CITY);
            throw new SavingOrderException(WRONG_CITY);
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
