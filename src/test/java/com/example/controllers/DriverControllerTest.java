package com.example.controllers;

import com.example.FreightApplication;
import com.example.models.DriverDto;
import com.example.models.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = FreightApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"/application-test.properties"})
@ActiveProfiles("test")
public class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverController driverController;

    private static DriverDto addingDriver, updatingDriver;

    @BeforeClass
    public static void setUp() {
        addingDriver = new DriverDto();
        UserDto userDto = new UserDto();
        userDto.setPassword("password");
        userDto.setFirstName("Shkodran");
        userDto.setLastName("Mustafi");
        userDto.setEmail("mustafi@mail.ru");
        addingDriver.setUserDto(userDto);

        updatingDriver = new DriverDto();
        UserDto userDto1 = new UserDto();
        userDto1.setPassword("password");
        userDto1.setFirstName("Therry");
        userDto1.setLastName("Henry");
        userDto1.setEmail("henry@gmail.ru");
        updatingDriver.setUserDto(userDto1);
    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(get("/drivers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userDto.login").value("driver_1"))
                .andExpect(jsonPath("$[1].userDto.login").value("driver_2"))
                .andExpect(jsonPath("$[2].userDto.login").value("driver_3"))
                .andExpect(jsonPath("$[3].userDto.login").value("driver_4"));
    }

    @Test
    public void findByIdSuccessfully() throws Exception {
        mockMvc.perform(get("/drivers/4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userDto.login").value("driver_4"));
    }

    @Test
    public void failedFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/drivers/77").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Driver with id: 77 not found"));
    }

    @Test
    public void getFreeDrivers() throws Exception {
        mockMvc.perform(get("/drivers/free").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userDto.login").value("driver_1"))
                .andExpect(jsonPath("$[1].userDto.login").value("driver_2"))
                .andExpect(jsonPath("$[2].userDto.login").value("driver_4"))
                .andExpect(jsonPath("$[0].status").value("REST"))
                .andExpect(jsonPath("$[1].status").value("REST"))
                .andExpect(jsonPath("$[2].status").value("REST"))
                .andDo(print());
    }

    @Test
    public void updateDriverSuccessfully() throws Exception {
        updatingDriver.setId(2);
        updatingDriver.setDriverLicense("1020304050");
        updatingDriver.getUserDto().setLogin("driver_2");
        updatingDriver.getUserDto().setId(2);

        String str = new ObjectMapper().writeValueAsString(updatingDriver);

        mockMvc.perform(put("/drivers").contentType(MediaType.APPLICATION_JSON).content(str))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    public void failedUpdateDriverDriverLicenseExists() throws Exception {
        updatingDriver.setId(2);
        updatingDriver.setDriverLicense("0102030405");
        updatingDriver.getUserDto().setLogin("driver_2");
        updatingDriver.getUserDto().setId(2);

        String str = new ObjectMapper().writeValueAsString(updatingDriver);

        mockMvc.perform(put("/drivers").contentType(MediaType.APPLICATION_JSON).content(str))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Driver with driver license: 0102030405 already exist"));
    }

    @Test
    public void failedUpdateDriverUserIdZero() throws Exception {
        updatingDriver.setId(2);
        updatingDriver.setDriverLicense("0102030405");
        updatingDriver.getUserDto().setLogin("driver_2");

        String str = new ObjectMapper().writeValueAsString(updatingDriver);

        mockMvc.perform(put("/drivers").contentType(MediaType.APPLICATION_JSON).content(str))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User id cannot be equals or less than 0: 0"));
    }

    @Test
    public void addDriverSuccessfully() throws Exception {
        addingDriver.setDriverLicense("0102030409");
        addingDriver.getUserDto().setLogin("driver_10");

        String str = new ObjectMapper().writeValueAsString(addingDriver);

        mockMvc.perform(post("/drivers").contentType(MediaType.APPLICATION_JSON)
                .content(str))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    public void failedAddDriverLoginExists() throws Exception {
        addingDriver.setDriverLicense("0102030409");
        addingDriver.getUserDto().setLogin("driver_2");

        String str = new ObjectMapper().writeValueAsString(addingDriver);

        mockMvc.perform(post("/drivers").contentType(MediaType.APPLICATION_JSON)
                .content(str))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Driver with login: driver_2 already exist"));
    }

    @Test
    public void failedAddDriverDriverLicenseExists() throws Exception {
        addingDriver.setDriverLicense("1020304050");
        addingDriver.getUserDto().setLogin("driver_7");

        String str = new ObjectMapper().writeValueAsString(addingDriver);

        mockMvc.perform(post("/drivers").contentType(MediaType.APPLICATION_JSON)
                .content(str))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Driver with driver license: 1020304050 already exist"));
    }
}
