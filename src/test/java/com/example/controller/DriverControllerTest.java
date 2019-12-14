package com.example.controller;

import com.example.FreightApplication;
import com.example.security.models.LoginInfo;
import com.example.services.models.FullInfoDriverDto;
import com.example.services.models.FullInfoUserDto;
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
import org.springframework.util.LinkedMultiValueMap;

import static com.example.services.commons.message.DriverExceptionMessage.DRIVER_NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = FreightApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"/application-test.properties"})
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.JVM)
public class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverController driverController;

    private FullInfoDriverDto addingDriver, updatingDriver;
    private static String managerToken, driverToken;

    @Before
    public void setUpDrivers() {
        addingDriver = new FullInfoDriverDto();
        FullInfoUserDto userDto = new FullInfoUserDto();
        userDto.setPassword("password");
        userDto.setFirstName("Shkodran");
        userDto.setLastName("Mustafi");
        userDto.setEmail("mustafi@mail.ru");
        addingDriver.setUser(userDto);

        updatingDriver = new FullInfoDriverDto();
        FullInfoUserDto userDto1 = new FullInfoUserDto();
        userDto1.setPassword("password");
        userDto1.setFirstName("Therry");
        userDto1.setLastName("Henry");
        userDto1.setEmail("henry@gmail.ru");
        updatingDriver.setUser(userDto1);
    }


    @Before
    public void setUpToken() throws Exception {
        LoginInfo info = new LoginInfo();
        info.setLogin("manager_1");
        info.setPassword("password");

        String obj = new ObjectMapper().writeValueAsString(info);
        MvcResult result = mockMvc
                .perform(post("/login").content(obj))
                .andReturn();

        int length = result.getResponse().getContentAsString().length();
        managerToken = "Bearer " + result.getResponse().getContentAsString().substring(1, length - 1);

        info = new LoginInfo();
        info.setLogin("driver_1");
        info.setPassword("password");

        obj = new ObjectMapper().writeValueAsString(info);
        result = mockMvc
                .perform(post("/login").content(obj))
                .andReturn();

        length = result.getResponse().getContentAsString().length();
        driverToken = "Bearer " + result.getResponse().getContentAsString().substring(1, length - 1);
    }

    @Test
    public void getDriversBySearch() throws Exception {
        mockMvc.perform(
                get("/drivers/search")
                        .header("Authorization", managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("text", "")
                        .param("page", "1")
                        .param("size", "20"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(13));
    }

    @Test
    public void findByIdSuccessfullyByManager() throws Exception {
        mockMvc.perform(
                get("/drivers/4")
                        .header("Authorization", managerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driverLicense").value("9080706050"));
    }

    @Test
    public void findByIdSuccessfullyByDriver() throws Exception {
        mockMvc.perform(
                get("/drivers/1")
                        .header("Authorization", driverToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driverLicense").value("0102030405"));
    }

    @Test
    public void failedFindByIdNotFound() throws Exception {
        mockMvc.perform(
                get("/drivers/77")
                        .header("Authorization", managerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(DRIVER_NOT_FOUND, 77)));
    }

    @Test
    public void getFreeDrivers() throws Exception {
        mockMvc.perform(
                get("/drivers/free")
                        .header("Authorization", managerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].status").value("REST"))
                .andExpect(jsonPath("$[1].status").value("REST"))
                .andExpect(jsonPath("$[2].status").value("REST"));
    }

    @Test
    public void updateDriverSuccessfully() throws Exception {
        updatingDriver.setId(2L);
        updatingDriver.setDriverLicense("1020304050");
        updatingDriver.getUser().setLogin("driver_2");
        updatingDriver.getUser().setId(2L);

        String str = new ObjectMapper().writeValueAsString(updatingDriver);

        mockMvc.perform(
                put("/drivers")
                        .header("Authorization", managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    public void failedUpdateDriverDriverLicenseExists() throws Exception {
        updatingDriver.setId(2L);
        updatingDriver.setDriverLicense("0102030405");
        updatingDriver.getUser().setLogin("driver_2");
        updatingDriver.getUser().setId(2L);

        String str = new ObjectMapper().writeValueAsString(updatingDriver);

        mockMvc.perform(
                put("/drivers")
                        .header("Authorization", managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Driver with driver license: 0102030405 already exist"));
    }

    @Test
    public void failedUpdateDriverUserIdZero() throws Exception {
        updatingDriver.setId(2L);
        updatingDriver.getUser().setId(0L);
        updatingDriver.setDriverLicense("0102030405");
        updatingDriver.getUser().setLogin("driver_2");

        String str = new ObjectMapper().writeValueAsString(updatingDriver);

        mockMvc.perform(
                put("/drivers")
                        .header("Authorization", managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("User id cannot be equals or less than 0: 0"));
    }

    @Test
    public void addDriverSuccessfully() throws Exception {
        addingDriver.setDriverLicense("0102030409");
        addingDriver.getUser().setLogin("driver_90");

        String str = new ObjectMapper().writeValueAsString(addingDriver);

        mockMvc.perform(
                post("/drivers")
                        .header("Authorization", managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value("true"));
    }

    @Test
    public void failedAddDriverLoginExists() throws Exception {
        addingDriver.setDriverLicense("0102030409");
        addingDriver.getUser().setLogin("driver_2");

        String str = new ObjectMapper().writeValueAsString(addingDriver);

        mockMvc.perform(
                post("/drivers")
                        .header("Authorization", managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Driver with login: driver_2 already exist"));
    }

    @Test
    public void failedAddDriverDriverLicenseExists() throws Exception {
        addingDriver.setDriverLicense("1020304050");
        addingDriver.getUser().setLogin("driver_99");

        String str = new ObjectMapper().writeValueAsString(addingDriver);

        mockMvc.perform(
                post("/drivers")
                        .header("Authorization", managerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Driver with driver license: 1020304050 already exist"));
    }
}
