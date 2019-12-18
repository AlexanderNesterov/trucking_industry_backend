package com.example.controller;

import com.example.FreightApplication;
import com.example.security.models.LoginInfo;
import com.example.services.models.CityDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
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

import static com.example.services.commons.message.CityExceptionMessage.SAVING_CITY_ERROR;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = FreightApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"/application-test.properties"})
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.JVM)
public class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String token;
    private CityDto addingCity;


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

    @Before
    public void setUpCity() {
        addingCity = new CityDto();
        addingCity.setName("Paris");
        addingCity.setCountry("France");
        addingCity.setLatitude(78);
        addingCity.setLatitude(113);
    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(
                get("/trucking-industry/cities")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(8));
    }

    @Test
    public void addCity_SuitableCity_True() throws Exception {
        String str = new ObjectMapper().writeValueAsString(addingCity);

        mockMvc.perform(
                post("/trucking-industry/cities")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    public void addCity_CityExists_BadRequest() throws Exception {
        addingCity.setName("Voronezh");
        addingCity.setCountry("Russia");

        String str = new ObjectMapper().writeValueAsString(addingCity);

        mockMvc.perform(
                post("/trucking-industry/cities")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(str))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.message")
                        .value(String.format(SAVING_CITY_ERROR, addingCity.getName())));
    }
}
