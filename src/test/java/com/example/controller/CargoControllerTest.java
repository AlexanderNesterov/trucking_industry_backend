package com.example.controller;

import com.example.FreightApplication;
import com.example.database.models.commons.CargoStatus;
import com.example.services.models.CargoDto;
import com.example.services.models.DriverDto;
import com.example.services.models.TruckDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = FreightApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"/application-test.properties"})
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.JVM)
public class CargoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CargoController cargoController;

    private static CargoDto updatingCargo;

    @BeforeClass
    public static void setUp() {
        updatingCargo = new CargoDto();
        updatingCargo.setId(5L);
        updatingCargo.setTitle("Steel");
        updatingCargo.setTruck(new TruckDto());
        updatingCargo.setDriver(new DriverDto());
        updatingCargo.setCoDriver(new DriverDto());
    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(get("/cargo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    public void findByIdSuccessfully() throws Exception {
        mockMvc.perform(get("/cargo/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Chocolate"))
                .andExpect(jsonPath("$.driver.id").value(6))
                .andExpect(jsonPath("$.coDriver.id").value(7));
    }

    @Test
    public void failedFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/cargo/10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cargo with id: 10 not found"));
    }

    @Test
    public void getCargoByDriverIdSuccessfully() throws Exception {
        mockMvc.perform(get("/cargo/for-driver/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Water"))
                .andExpect(jsonPath("$.weight").value(500))
                .andExpect(jsonPath("$.status").value(CargoStatus.IN_PROGRESS.name()));
    }

    @Test
    public void failedGetCargoByDriverId() throws Exception {
        mockMvc.perform(get("/cargo/for-driver/101"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cargo with driver id: 101 not found"));
    }

    @Test
    public void setAcceptStatusSuccessfully() throws Exception {
        mockMvc.perform(put("/cargo/set-accept-status/2/6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    public void failedSetAcceptStatusCoDriverId() throws Exception {
        mockMvc.perform(put("/cargo/set-accept-status/2/7"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Driver with id: 7 is not main driver for cargo with id: 2"));
    }

    @Test
    public void failedSetAcceptStatusWrongDriver() throws Exception {
        mockMvc.perform(put("/cargo/set-accept-status/2/12"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Driver with id: 12 is not included in cargo with id: 2"));
    }

    @Test
    public void failedSetAcceptStatusWrongCargoStatus() throws Exception {
        mockMvc.perform(put("/cargo/set-accept-status/1/3"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Attempt to set ACCEPT status to wrong cargo"));
    }

    @Test
    public void setRefusedStatusSuccessfully() throws Exception {
        mockMvc.perform(put("/cargo/set-refuse-status/3/8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    public void setDeliveredStatusSuccessfully() throws Exception {
        mockMvc.perform(put("/cargo/set-deliver-status/4/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    public void updateCargoSuccessfully() throws Exception {
        updatingCargo.setWeight(300);
        updatingCargo.getTruck().setId(6L);
        updatingCargo.getDriver().setId(1L);
        updatingCargo.getCoDriver().setId(2L);

        String str = new ObjectMapper().writeValueAsString(updatingCargo);

        mockMvc.perform(put("/cargo").contentType(MediaType.APPLICATION_JSON).content(str))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value("true"));
    }
}
