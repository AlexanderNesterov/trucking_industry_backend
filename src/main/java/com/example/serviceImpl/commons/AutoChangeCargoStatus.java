package com.example.serviceImpl.commons;

import com.example.services.CargoService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class AutoChangeCargoStatus {

    private final CargoService cargoService;

    public AutoChangeCargoStatus(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @Scheduled(fixedRate = 60_000)
    public void changeStatus() {
        cargoService.autoChangeCargoStatus();
    }
}
