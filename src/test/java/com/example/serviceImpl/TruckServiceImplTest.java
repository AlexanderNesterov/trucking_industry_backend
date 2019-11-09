package com.example.serviceImpl;

import com.example.controllers.exceptions.RegistrationNumberExistsException;
import com.example.controllers.exceptions.TruckNotFoundException;
import com.example.database.models.commons.TruckCondition;
import com.example.database.repositories.TruckRepository;
import com.example.models.TruckDto;
import com.example.services.TruckService;
import com.example.services.mappers.TruckMapper;
import com.example.services.mappers.TruckMapperImpl;
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

    @InjectMocks
    private TruckService truckService = new TruckServiceImpl(truckMapper);

    @Mock
    private TruckRepository truckRepository;

    @Test
    public void addTruckSuccessfully() {
        TruckDto truckDto = new TruckDto();

        Mockito.when(truckRepository.getTruckByRegistrationNumber(truckDto.getRegistrationNumber())).thenReturn(null);
        boolean t = truckService.addTruck(truckDto);

        assertEquals("SERVICEABLE", truckDto.getCondition().name());
        assertEquals(0, truckDto.getId());
        assertTrue(t);
    }

    @Test(expected = RegistrationNumberExistsException.class)
    public void failedAddTruckSameRegistrationNumbers() {
        TruckDto truckDto = new TruckDto();
        truckDto.setRegistrationNumber("BB89009");

        TruckDto existsTruck = new TruckDto();
        existsTruck.setRegistrationNumber("BB89009");

        Mockito.when(truckRepository.getTruckByRegistrationNumber(truckDto.getRegistrationNumber())).
                thenReturn(truckMapper.fromDto(existsTruck));

        truckService.addTruck(truckDto);
    }

    @Test
    public void updateTruckWithNewRegistrationNumberSuccessfully() {
        TruckDto savingTruck = new TruckDto();
        savingTruck.setId(23);
        savingTruck.setRegistrationNumber("AA89009");

        TruckDto sameTruck = new TruckDto();
        sameTruck.setId(23);
        sameTruck.setRegistrationNumber("BB89009");
        sameTruck.setCondition(TruckCondition.SERVICEABLE);

        Mockito.when(truckRepository.getTruckByRegistrationNumber("AA89009")).thenReturn(null);
        Mockito.when(truckRepository.findById(23)).thenReturn(Optional.of(truckMapper.fromDto(sameTruck)));
        boolean result = truckService.updateTruck(savingTruck);

        assertEquals(TruckCondition.SERVICEABLE, savingTruck.getCondition());
        assertTrue(result);
    }

    @Test
    public void successUpdateTruckWithoutRegistrationNumberSuccessfully() {
        TruckDto savingTruck = new TruckDto();
        savingTruck.setId(23);
        savingTruck.setModel("Mercedes C-1000");
        savingTruck.setRegistrationNumber("AA89009");
        savingTruck.setCapacity(1000);

        TruckDto sameTruck = new TruckDto();
        sameTruck.setId(23);
        sameTruck.setModel("Renault t-500");
        sameTruck.setRegistrationNumber("AA89009");
        sameTruck.setCapacity(300);
        sameTruck.setCondition(TruckCondition.FAULTY);

        Mockito.when(truckRepository.getTruckByRegistrationNumber("AA89009")).thenReturn(truckMapper.fromDto(sameTruck));
        Mockito.when(truckRepository.findById(23)).thenReturn(Optional.of(truckMapper.fromDto(sameTruck)));
        boolean result = truckService.updateTruck(savingTruck);

        assertEquals("Mercedes C-1000", savingTruck.getModel());
        assertEquals(1000, savingTruck.getCapacity());
        assertEquals(TruckCondition.FAULTY, savingTruck.getCondition());
        assertTrue(result);
    }

    @Test(expected = RegistrationNumberExistsException.class)
    public void failedUpdateTruckRegistrationNumberExists() {
        TruckDto savingTruck = new TruckDto();
        savingTruck.setId(23);
        savingTruck.setRegistrationNumber("AA89009");

        TruckDto sameTruck = new TruckDto();
        sameTruck.setId(90);
        sameTruck.setRegistrationNumber("AA89009");
        sameTruck.setCondition(TruckCondition.FAULTY);

        Mockito.when(truckRepository.getTruckByRegistrationNumber("AA89009")).thenReturn(truckMapper.fromDto(sameTruck));
        Mockito.when(truckRepository.findById(23)).thenReturn(Optional.of(truckMapper.fromDto(sameTruck)));
        truckService.updateTruck(savingTruck);
    }
    @Test
    public void findByIdSuccessfully() {
        TruckDto truckDto = new TruckDto();
        truckDto.setId(12);
        truckDto.setModel("Renault t-500");
        truckDto.setRegistrationNumber("BB89009");
        truckDto.setCapacity(300);

        Mockito.when(truckRepository.findById(12)).thenReturn(Optional.of(truckMapper.fromDto(truckDto)));
        TruckDto foundTruck = truckService.findById(12);

        assertEquals("BB89009", foundTruck.getRegistrationNumber());
        assertEquals("Renault t-500", foundTruck.getModel());
        assertEquals(300, foundTruck.getCapacity());
    }

    @Test(expected = TruckNotFoundException.class)
    public void failedFindByIdNotFound() {
        Mockito.when(truckRepository.findById(10)).thenReturn(Optional.empty());
        truckService.findById(10);
    }
}
