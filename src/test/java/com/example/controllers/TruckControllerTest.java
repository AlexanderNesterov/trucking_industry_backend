package com.example.controllers;


import com.example.FreightApplication;
import com.example.models.TruckDto;
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

    @Autowired
    private TruckController truckController;

    private static TruckDto updatingTruck, addingTruck;

    @BeforeClass
    public static void setUp() {
        updatingTruck = new TruckDto();
        updatingTruck.setModel("MAZ 900");
        updatingTruck.setCapacity(450);

        addingTruck = new TruckDto();
        addingTruck.setModel("Scania G-500");
        addingTruck.setCapacity(550);
    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(get("/trucks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].registrationNumber").value("BB90029"))
                .andExpect(jsonPath("$[1].registrationNumber").value("BV87524"))
                .andExpect(jsonPath("$[4].registrationNumber").value("KO89029"))
                .andExpect(jsonPath("$.length()").value(6));
    }

    @Test
    public void getFreeTrucks() throws Exception {
        mockMvc.perform(get("/trucks/free/450").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].registrationNumber").value("JK65243"));
    }

    @Test
    public void findByIdSuccessfully() throws Exception {
        mockMvc.perform(get("/trucks/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNumber").value("JK65243"));
    }

    @Test
    public void failedFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/trucks/10").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Truck with id: 10 not found"));
    }

    @Test
    public void updateTruckSuccessfully() throws Exception {
        updatingTruck.setId(3);
        updatingTruck.setRegistrationNumber("JK65243");

        String str = new ObjectMapper().writeValueAsString(updatingTruck);

        mockMvc.perform(put("/trucks").contentType(MediaType.APPLICATION_JSON).content(str))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    public void failedUpdateTruckRegistrationNumberExists() throws Exception {
        updatingTruck.setId(3);
        updatingTruck.setRegistrationNumber("BB90029");

        String str = new ObjectMapper().writeValueAsString(updatingTruck);

        mockMvc.perform(put("/trucks").contentType(MediaType.APPLICATION_JSON).content(str))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Truck with registration number: BB90029 already exists"));
    }

    @Test
    public void addTruckSuccessfully() throws Exception {
        addingTruck.setRegistrationNumber("NM90878");

        String str = new ObjectMapper().writeValueAsString(addingTruck);

        mockMvc.perform(post("/trucks").contentType(MediaType.APPLICATION_JSON).content(str))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    public void failedAddTruckRegistrationNumberExists() throws Exception {
        addingTruck.setRegistrationNumber("JK65243");

        String str = new ObjectMapper().writeValueAsString(addingTruck);

        mockMvc.perform(post("/trucks").contentType(MediaType.APPLICATION_JSON).content(str))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Truck with registration number: JK65243 already exists"));
    }
}
