package com.example.controller;

import com.example.FreightApplication;
import com.example.database.models.commons.CargoStatus;
import com.example.security.models.LoginInfo;
import com.example.services.models.CargoDto;
import com.example.services.models.DriverDto;
import com.example.services.models.TruckDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
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
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    private String setUpToken(Long id, boolean isDriver) throws Exception {
        LoginInfo info = new LoginInfo();

        if (isDriver) {
            info.setLogin("driver_" + id);
        } else {
            info.setLogin("manager_" + id);
        }

        info.setPassword("password");

        String obj = new ObjectMapper().writeValueAsString(info);
        MvcResult result = mockMvc
                .perform(post("/login").content(obj))
                .andReturn();

        int length = result.getResponse().getContentAsString().length();
        return "Bearer " + result.getResponse().getContentAsString().substring(1, length - 1);
    }

    @Test
    public void findAll() throws Exception {
        String token = setUpToken(1L, false);

        mockMvc.perform(
                get("/cargo")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    public void findByIdSuccessfully() throws Exception {
        String token = setUpToken(1L, false);

        mockMvc.perform(
                get("/cargo/2")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Chocolate"))
                .andExpect(jsonPath("$.driver.id").value(6))
                .andExpect(jsonPath("$.coDriver.id").value(7));
    }

    @Test
    public void failedFindByIdNotFound() throws Exception {
        String token = setUpToken(1L, false);

        mockMvc.perform(
                get("/cargo/10")
                        .header("Authorization", token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cargo with id: 10 not found"));
    }

    @Test
    public void getCargoByDriverIdSuccessfully() throws Exception {
        Long driverId = 3L;
        String url = String.format("/cargo/for-driver/%d", driverId);
        String token = setUpToken(driverId, true);

        mockMvc.perform(
                get(url)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Water"))
                .andExpect(jsonPath("$.weight").value(500))
                .andExpect(jsonPath("$.status").value(CargoStatus.IN_PROGRESS.name()));
    }

    @Test
    public void failedGetCargoByDriverId() throws Exception {
        Long driverId = 4L;
        String url = String.format("/cargo/for-driver/%d", driverId);
        String token = setUpToken(driverId, true);

        mockMvc.perform(
                get(url)
                        .header("Authorization", token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cargo with driver id: 4 not found"));
    }

    @Test
    public void setAcceptStatusSuccessfully() throws Exception {
        Long driverId = 6L;
        String url = String.format("/cargo/set-accept-status/2/%d", driverId);
        String token = setUpToken(driverId, true);

        mockMvc.perform(
                put(url)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    public void failedSetAcceptStatusCoDriverId() throws Exception {
        Long driverId = 7L;
        String url = String.format("/cargo/set-accept-status/2/%d", driverId);
        String token = setUpToken(driverId, true);

        mockMvc.perform(
                put(url)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Wrong cargo id or main driver id"));
    }

    @Test
    public void failedSetAcceptStatusWrongDriver() throws Exception {
        Long driverId = 12L;
        String token = setUpToken(driverId, true);
        String url = String.format("/cargo/set-accept-status/2/%d", driverId);

        mockMvc.perform(
                put(url)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Wrong cargo id or main driver id"));
    }

    @Test
    public void failedSetAcceptStatusWrongCargoStatus() throws Exception {
        Long driverId = 3L;
        String url = String.format("/cargo/set-accept-status/1/%d", driverId);
        String token = setUpToken(driverId, true);

        mockMvc.perform(
                put(url)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Attempt to set ACCEPT status to wrong cargo"));
    }

    @Test
    public void setRefusedStatusSuccessfully() throws Exception {
        Long driverId = 8L;
        String url = String.format("/cargo/set-refuse-status/3/%d", driverId);
        String token = setUpToken(driverId, true);

        mockMvc.perform(
                put(url)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    public void setDeliveredStatusSuccessfully() throws Exception {
        Long driverId = 10L;
        String url = String.format("/cargo/set-deliver-status/4/%d", driverId);
        String token = setUpToken(driverId, true);

        mockMvc.perform(
                put(url)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    public void updateCargoSuccessfully() throws Exception {
        String token = setUpToken(1L, false);

        updatingCargo.setWeight(300);
        updatingCargo.getTruck().setId(6L);
        updatingCargo.getDriver().setId(1L);
        updatingCargo.getCoDriver().setId(2L);

        String str = new ObjectMapper().writeValueAsString(updatingCargo);

        mockMvc.perform(
                put("/cargo")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value("true"));
    }
}
