package com.example.controller;

import com.example.FreightApplication;
import com.example.database.models.commons.CargoStatus;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.OrderStatus;
import com.example.database.models.commons.TruckCondition;
import com.example.security.models.LoginInfo;
import com.example.services.models.*;
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

import java.util.Arrays;

import static com.example.services.commons.message.OrderExceptionMessage.*;
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
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static OrderDto updatingOrder;

    @Before
    public void setUp() {
        SimpleDriverDto driver = new SimpleDriverDto();
        driver.setId(1L);
        driver.setStatus(DriverStatus.ASSIGNED);
        driver.setDriverLicense("1020304050");
        driver.setUser(new SimpleUserDto());

        SimpleDriverDto coDriver = new SimpleDriverDto();
        coDriver.setId(2L);
        coDriver.setStatus(DriverStatus.ASSIGNED);
        coDriver.setDriverLicense("1020304060");
        coDriver.setUser(new SimpleUserDto());

        TruckDto truck = new TruckDto();
        truck.setId(6L);
        truck.setCondition(TruckCondition.SERVICEABLE);
        truck.setCapacity(1000);

        CityDto firstCity = new CityDto();
        firstCity.setId(2L);
        CityDto secondCity = new CityDto();
        secondCity.setId(5L);
        CityDto thirdCity = new CityDto();
        thirdCity.setId(8L);
        CityDto fourthCity = new CityDto();
        fourthCity.setId(1L);

        CargoDto firstCargo = new CargoDto();
        firstCargo.setTitle("Water");
        firstCargo.setWeight(200);
        firstCargo.setStatus(CargoStatus.CREATED);
        firstCargo.setLoadLocation(secondCity);
        firstCargo.setDischargeLocation(firstCity);

        CargoDto secondCargo = new CargoDto();
        secondCargo.setTitle("Fuel");
        secondCargo.setWeight(150);
        secondCargo.setStatus(CargoStatus.CREATED);
        secondCargo.setLoadLocation(thirdCity);
        secondCargo.setDischargeLocation(fourthCity);

        CargoDto thirdCargo = new CargoDto();
        thirdCargo.setTitle("Food");
        thirdCargo.setWeight(300);
        thirdCargo.setStatus(CargoStatus.CREATED);
        thirdCargo.setLoadLocation(firstCity);
        thirdCargo.setDischargeLocation(thirdCity);

        updatingOrder = new OrderDto();
        updatingOrder.setId(5L);
        updatingOrder.setDriver(driver);
        updatingOrder.setCoDriver(coDriver);
        updatingOrder.setTruck(truck);
        updatingOrder.setCargoList(Arrays.asList(firstCargo, secondCargo, thirdCargo));
        updatingOrder.setTotalWeight(650);
        updatingOrder.setStatus(OrderStatus.CREATED);
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
                .perform(post("/trucking-industry/login").content(obj))
                .andReturn();

        int length = result.getResponse().getContentAsString().length();
        return "Bearer " + result.getResponse().getContentAsString().substring(1, length - 1);
    }

    @Test
    public void findAll() throws Exception {
        String token = setUpToken(1L, false);

        mockMvc.perform(
                get("/trucking-industry/order/search")
                        .header("Authorization", token)
                        .param("text", "")
                        .param("page", "1")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    public void findByIdSuccessfully() throws Exception {
        String token = setUpToken(1L, false);

        mockMvc.perform(
                get("/trucking-industry/order/2")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driver.id").value(6))
                .andExpect(jsonPath("$.coDriver.id").value(7));
    }

    @Test
    public void failedFindByIdNotFound() throws Exception {
        String token = setUpToken(1L, false);

        mockMvc.perform(
                get("/trucking-industry/order/10")
                        .header("Authorization", token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(ORDER_NOT_FOUND, 10L)));
    }

    @Test
    public void getCargoByDriverIdSuccessfully() throws Exception {
        Long driverId = 3L;
        String url = String.format("/trucking-industry/order/for-driver/%d", driverId);
        String token = setUpToken(driverId, true);

        mockMvc.perform(
                get(url)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalWeight").value(400))
                .andExpect(jsonPath("$.status").value(CargoStatus.IN_PROGRESS.name()));
    }

    @Test
    public void failedGetCargoByDriverId() throws Exception {
        Long driverId = 4L;
        String url = String.format("/trucking-industry/order/for-driver/%d", driverId);
        String token = setUpToken(driverId, true);

        mockMvc.perform(
                get(url)
                        .header("Authorization", token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(ORDER_BY_DRIVER_NOT_FOUND, driverId)));
    }

    @Test
    public void setAcceptStatusSuccessfully() throws Exception {
        Long driverId = 6L;
        String url = String.format("/trucking-industry/order/set-accept-status/2/%d", driverId);
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
        Long orderId = 2L;
        String url = String.format("/trucking-industry/order/set-accept-status/%d/%d",orderId, driverId);
        String token = setUpToken(driverId, true);

        mockMvc.perform(
                put(url)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(String.format(WRONG_ORDER_OR_DRIVER, orderId, driverId)));
    }

    @Test
    public void failedSetAcceptStatusWrongDriver() throws Exception {
        Long driverId = 12L;
        Long orderId = 2L;
        String token = setUpToken(driverId, true);
        String url = String.format("/trucking-industry/order/set-accept-status/%d/%d", orderId, driverId);

        mockMvc.perform(
                put(url)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(String.format(WRONG_ORDER_OR_DRIVER, orderId, driverId)));
    }

    @Test
    public void failedSetAcceptStatusWrongCargoStatus() throws Exception {
        Long driverId = 3L;
        Long orderId = 1L;
        String url = String.format("/trucking-industry/order/set-accept-status/%d/%d", orderId, driverId);
        String token = setUpToken(driverId, true);

        mockMvc.perform(
                put(url)
                        .header("Authorization", token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(String.format(WRONG_ORDER_STATUS, OrderStatus.IN_PROGRESS)));
    }

    @Test
    public void setRefusedStatusSuccessfully() throws Exception {
        Long driverId = 8L;
        String url = String.format("/trucking-industry/order/set-refuse-status/3/%d", driverId);
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

/*        updatingOrder.setTotalWeight(300);
        updatingOrder.getTruck().setId(6L);
        updatingOrder.getDriver().setId(1L);
        updatingOrder.getCoDriver().setId(2L);*/

        String str = new ObjectMapper().writeValueAsString(updatingOrder);

        mockMvc.perform(
                put("/trucking-industry/order")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value("true"));
    }
}
