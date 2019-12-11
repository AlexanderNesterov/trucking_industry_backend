package com.example.services.serviceImpl;

import com.example.controller.exceptions.TruckExistsException;
import com.example.controller.exceptions.TruckNotFoundException;
import com.example.database.models.commons.TruckCondition;
import com.example.database.repositories.TruckRepository;
import com.example.services.models.TruckDto;
import com.example.services.TruckService;
import com.example.services.mappers.TruckMapper;
import com.example.services.mappers.TruckMapperImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class TruckServiceImplTest {

    private TruckMapper truckMapper = new TruckMapperImpl();
    private TruckDto addingTruck, updatingTruck;

    @InjectMocks
    private TruckService truckService = new TruckServiceImpl(truckMapper);

    @Mock
    private TruckRepository truckRepository;

    @Before
    public void setUp() {
        addingTruck = new TruckDto();
        addingTruck.setModel("Scania S-900");
        addingTruck.setCapacity(700);

        updatingTruck = new TruckDto();
        updatingTruck.setModel("Mercedes C-500");
        updatingTruck.setCapacity(500);
    }

    @Test
    public void addTruckSuccessfully() {
        addingTruck.setRegistrationNumber("IL90019");

        Mockito
                .when(truckRepository.getTruckByRegistrationNumber(addingTruck.getRegistrationNumber()))
                .thenReturn(null);
        boolean t = truckService.addTruck(addingTruck);

        assertEquals("SERVICEABLE", addingTruck.getCondition().name());
        assertNull(addingTruck.getId());
        assertTrue(t);
    }

    @Test
    public void failedAddTruckSameRegistrationNumbers() {
        addingTruck.setRegistrationNumber("BB89009");

        TruckDto existsTruck = new TruckDto();
        existsTruck.setRegistrationNumber("BB89009");

        Mockito
                .when(truckRepository.getTruckByRegistrationNumber(addingTruck.getRegistrationNumber())).
                thenReturn(truckMapper.fromDto(existsTruck));

        TruckExistsException thrown = assertThrows(TruckExistsException.class,
                () -> truckService.addTruck(this.addingTruck));

        assertTrue(thrown.getMessage()
                .contains("Truck with registration number: " + addingTruck.getRegistrationNumber() + " already exists"));
    }

    @Test
    public void updateTruckWithNewRegistrationNumberSuccessfully() {
        updatingTruck.setId(23L);
        updatingTruck.setRegistrationNumber("AA89009");

        TruckDto sameTruck = new TruckDto();
        sameTruck.setId(23L);
        sameTruck.setRegistrationNumber("BB89009");
        sameTruck.setCondition(TruckCondition.SERVICEABLE);

        Mockito
                .when(truckRepository.getTruckByRegistrationNumber("AA89009"))
                .thenReturn(null);
        Mockito
                .when(truckRepository.findById(23L))
                .thenReturn(Optional.of(truckMapper.fromDto(sameTruck)));
        boolean result = truckService.updateTruck(updatingTruck);

        assertEquals(sameTruck.getCondition(), updatingTruck.getCondition());
        assertTrue(result);
    }

    @Test
    public void successUpdateTruckWithoutRegistrationNumberSuccessfully() {
        updatingTruck.setId(23L);
        updatingTruck.setRegistrationNumber("AA89009");

        TruckDto sameTruck = new TruckDto();
        sameTruck.setId(23L);
        sameTruck.setModel("Renault t-500");
        sameTruck.setRegistrationNumber("AA89009");
        sameTruck.setCapacity(300);
//        sameTruck.setCondition(TruckCondition.FAULTY);

        Mockito
                .when(truckRepository.getTruckByRegistrationNumber("AA89009"))
                .thenReturn(truckMapper.fromDto(sameTruck));
        Mockito
                .when(truckRepository.findById(23L))
                .thenReturn(Optional.of(truckMapper.fromDto(sameTruck)));
        boolean result = truckService.updateTruck(updatingTruck);

        assertEquals(updatingTruck.getCondition(), updatingTruck.getCondition());
        assertTrue(result);
    }

    @Test
    public void failedUpdateTruckRegistrationNumberExists() {
        updatingTruck.setId(23L);
        updatingTruck.setRegistrationNumber("AA89009");

        TruckDto sameTruck = new TruckDto();
        sameTruck.setId(90L);
        sameTruck.setRegistrationNumber("AA89009");
//        sameTruck.setCondition(TruckCondition.FAULTY);

        Mockito
                .when(truckRepository.getTruckByRegistrationNumber("AA89009"))
                .thenReturn(truckMapper.fromDto(sameTruck));
        Mockito
                .when(truckRepository.findById(23L))
                .thenReturn(Optional.of(truckMapper.fromDto(sameTruck)));

        TruckExistsException thrown = assertThrows(TruckExistsException.class,
                () -> truckService.updateTruck(updatingTruck));

        assertTrue(thrown.getMessage()
                .contains("Truck with registration number: " +
                        updatingTruck.getRegistrationNumber() + " already exists"));
    }
    @Test
    public void findByIdSuccessfully() {
        TruckDto truckDto = new TruckDto();
        truckDto.setId(12L);
        truckDto.setModel("Renault t-500");
        truckDto.setRegistrationNumber("BB89009");
        truckDto.setCapacity(300);

        Mockito
                .when(truckRepository.findById(12L))
                .thenReturn(Optional.of(truckMapper.fromDto(truckDto)));
        TruckDto foundTruck = truckService.findById(12L);

        assertEquals("BB89009", foundTruck.getRegistrationNumber());
        assertEquals("Renault t-500", foundTruck.getModel());
        assertEquals(300, foundTruck.getCapacity());
    }

    @Test
    public void failedFindByIdNotFound() {
        Mockito
                .when(truckRepository.findById(10L))
                .thenReturn(Optional.empty());

        TruckNotFoundException thrown = assertThrows(TruckNotFoundException.class,
                () -> truckService.findById(10L));

        assertTrue(thrown.getMessage().contains("Truck with id: " + 10 + " not found"));
    }
}
