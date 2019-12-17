package com.example.controller;

import com.example.FreightApplication;
import com.example.security.models.LoginInfo;
import com.example.services.models.TruckDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Order;
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

import static com.example.services.commons.message.TruckExceptionMessage.REGISTRATION_NUMBER_EXISTS;
import static com.example.services.commons.message.TruckExceptionMessage.TRUCK_NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = FreightApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"/application-test.properties"})
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.JVM)
public class TruckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static TruckDto updatingTruck, addingTruck;
    private static String token;

    @Before
    public void setUp() {
        updatingTruck = new TruckDto();
        updatingTruck.setModel("MAZ 900");
        updatingTruck.setCapacity(450);

        addingTruck = new TruckDto();
        addingTruck.setModel("Scania G-500");
        addingTruck.setCapacity(550);
    }

    @Before
    public void setUpToken() throws Exception {
        LoginInfo info = new LoginInfo();
        info.setLogin("manager_1");
        info.setPassword("password");

        String obj = new ObjectMapper().writeValueAsString(info);
        MvcResult result = mockMvc
                .perform(post("/trucking-industry/login").content(obj))
                .andReturn();

        int length = result.getResponse().getContentAsString().length();
        token = "Bearer " + result.getResponse().getContentAsString().substring(1, length - 1);
    }

    @Test
    public void getTrucksBySearch() throws Exception {
        mockMvc.perform(
                get("/trucking-industry/trucks/search")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", "")
                        .param("page", "1")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(6));
    }

    @Test
    @Order(1)
    public void getFreeTrucks() throws Exception {
        double weight = 450;
        String text = "";
        int page = 1;
        int size = 20;

        mockMvc.perform(
                get("/trucking-industry/trucks/free")
                        .param("weight", String.valueOf(weight))
                        .param("text", text)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void findByIdSuccessfully() throws Exception {
        mockMvc.perform(
                get("/trucking-industry/trucks/3")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNumber").value("JK65243"));
    }

    @Test
    public void failedFindByIdNotFound() throws Exception {
        mockMvc.perform(
                get("/trucking-industry/trucks/10")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(TRUCK_NOT_FOUND, 10L)));
    }

    @Test
    public void updateTruckSuccessfully() throws Exception {
        updatingTruck.setId(3L);
        updatingTruck.setRegistrationNumber("JK65243");

        String str = new ObjectMapper().writeValueAsString(updatingTruck);

        mockMvc.perform(
                put("/trucking-industry/trucks")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    public void failedUpdateTruckRegistrationNumberExists() throws Exception {
        updatingTruck.setId(3L);
        updatingTruck.setRegistrationNumber("BB90029");

        String str = new ObjectMapper().writeValueAsString(updatingTruck);

        mockMvc.perform(
                put("/trucking-industry/trucks")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(String.format(REGISTRATION_NUMBER_EXISTS, updatingTruck.getRegistrationNumber())));
    }

    @Test
    public void addTruckSuccessfully() throws Exception {
        addingTruck.setRegistrationNumber("NM90878");

        String str = new ObjectMapper().writeValueAsString(addingTruck);

        mockMvc.perform(
                post("/trucking-industry/trucks")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    public void failedAddTruckRegistrationNumberExists() throws Exception {
        addingTruck.setRegistrationNumber("JK65243");

        String str = new ObjectMapper().writeValueAsString(addingTruck);

        mockMvc.perform(
                post("/trucking-industry/trucks")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(String.format(REGISTRATION_NUMBER_EXISTS, addingTruck.getRegistrationNumber())));
    }
}
