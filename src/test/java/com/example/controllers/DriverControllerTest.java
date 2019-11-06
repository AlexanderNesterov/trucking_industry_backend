package com.example.controllers;

import com.example.FreightApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest/*(classes = FreightApplication.class)*/
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverController driverController;

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void addDriver() {

    }
}
