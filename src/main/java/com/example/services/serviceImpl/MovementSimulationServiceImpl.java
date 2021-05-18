package com.example.services.serviceImpl;

import com.example.database.models.commons.CargoStatus;
import com.example.services.CargoService;
import com.example.services.MovementSimulationService;
import com.example.services.OrderService;
import com.example.services.TruckService;
import com.example.services.models.CargoDto;
import com.example.services.models.CityDto;
import com.example.services.models.OrderDto;
import com.example.services.models.TruckDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovementSimulationServiceImpl implements MovementSimulationService {

    public static final double delta = 0.0006;
    public static final double step = 0.0005;
    private final OrderService orderService;
    private final CargoService cargoService;
    private final TruckService truckService;
    private double k;
    private double b;

    public MovementSimulationServiceImpl(OrderService orderService, CargoService cargoService, TruckService truckService) {
        this.orderService = orderService;
        this.cargoService = cargoService;
        this.truckService = truckService;
    }

    @Override
    public void moveTrucks() {
        List<OrderDto> inProgressOrders = orderService.getInProgressOrders();

        for (OrderDto order : inProgressOrders) {
            TruckDto truck = order.getTruck();

            List<CargoDto> cargoList = cargoService.getCargoListByOrderId(order.getId()).stream()
                    .filter(cargoDto -> cargoDto.getStatus().equals(CargoStatus.IN_PROGRESS))
                    .collect(Collectors.toList());

            if (cargoList.size() != 0) {
                CargoDto cargo = cargoList.get(0);
                CityDto loadLocation = cargo.getLoadLocation();
                CityDto dischargeLocation = cargo.getDischargeLocation();

                if (truck.getLongitude() == 0.0 && truck.getLatitude() == 0.0) {
                    truck.setLatitude(loadLocation.getLatitude());
                    truck.setLongitude(loadLocation.getLongitude());
                }

                calculateFunctionParameters(loadLocation, dischargeLocation);

                boolean direction = loadLocation.getLongitude() < dischargeLocation.getLongitude();
                double x;
                double y;

                if (direction) {
                    x = truck.getLongitude() + step;
                } else {
                    x = truck.getLongitude() - step;
                }
                y = f(x);

                if (Math.abs(dischargeLocation.getLongitude() - x) > delta || Math.abs(dischargeLocation.getLatitude() - y) > delta) {
                    truck.setLongitude(x);
                    truck.setLatitude(y);
                    truckService.setNewPosition(truck.getId(), x, y);
                }
            }
        }
    }

    private void calculateFunctionParameters(CityDto loadLocation, CityDto dischargeLocation) {
        k = (-1) * (loadLocation.getLatitude() - dischargeLocation.getLatitude()) / (dischargeLocation.getLongitude() - loadLocation.getLongitude());
        b = (-1) * (loadLocation.getLongitude() * dischargeLocation.getLatitude() - dischargeLocation.getLongitude() * loadLocation.getLatitude()) / (dischargeLocation.getLongitude() - loadLocation.getLongitude());
    }

    private double f(double x) {
        return k * x + b;
    }
}
