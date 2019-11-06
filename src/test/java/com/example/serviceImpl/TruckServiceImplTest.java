package com.example.serviceImpl;

import com.example.database.repositories.TruckRepository;
import com.example.models.TruckDto;
import com.example.services.TruckService;
import com.example.services.mappers.TruckMapper;
import com.example.services.mappers.TruckMapperImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class TruckServiceImplTest {

    /*@InjectMocks
    private TruckService truckService = new TruckServiceImpl();
*/
    @Mock
    private TruckMapper truckMapper;

    @Mock
    private TruckRepository truckRepository;

    @Test
    public void addTruck() {
        TruckDto truckDto = new TruckDto();
        truckDto.setModel("Renault t-500");
        truckDto.setRegistrationNumber("BB89009");
        truckDto.setCapacity(300);

        //truckService.addTruck(truckDto);

        assertEquals("SERVICEABLE", truckDto.getCondition().name());
    }
}
