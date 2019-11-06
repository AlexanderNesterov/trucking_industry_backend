package com.example.serviceImpl;

import com.example.database.repositories.CargoRepository;
import com.example.models.CargoDto;
import com.example.models.DriverDto;
import com.example.models.TruckDto;
import com.example.services.CargoService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class CargoServiceImplTest {

    @Autowired
    private CargoService cargoService;

    @MockBean
    private CargoRepository cargoRepository;

    @Test
    void addCargo() {
        CargoDto cargoDto = new CargoDto();
        cargoDto.setTitle("Water");
        cargoDto.setWeight(200);

        DriverDto petr = new DriverDto();
        cargoDto.setDriverDto(petr);

        DriverDto anton = new DriverDto();
        cargoDto.setCoDriverDto(anton);

        TruckDto truckDto = new TruckDto();
        cargoDto.setTruckDto(truckDto);

        cargoService.addCargo(cargoDto);

        assertEquals("NOT_SELECTED", cargoDto.getDriverStatus().name());
        assertEquals("NOT_SELECTED", cargoDto.getCoDriverStatus().name());
        assertEquals("CREATED", cargoDto.getStatus().name());
    }

    @Test
    void setRefuseStatus() {
    }
}
