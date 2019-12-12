package com.example.services.serviceImpl;

import com.example.controller.exceptions.SavingTruckException;
import com.example.controller.exceptions.SetTruckConditionException;
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

import static com.example.services.commons.message.TruckExceptionMessage.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class TruckServiceImplTest {

    private TruckMapper truckMapper = new TruckMapperImpl();
    private TruckDto truckDto;

    @InjectMocks
    private TruckService sut = new TruckServiceImpl(truckMapper);

    @Mock
    private TruckRepository truckRepository;

    @Before
    public void setUp() {
        truckDto = new TruckDto();
        truckDto.setModel("Scania S-900");
        truckDto.setCapacity(700);
    }

    @Test
    public void findBy_IdExists_Truck() {
        Long truckId = 12L;
        truckDto.setId(truckId);

        Mockito
                .when(truckRepository.findById(truckDto.getId()))
                .thenReturn(Optional.of(truckMapper.fromDto(truckDto)));

        TruckDto foundTruck = sut.findById(truckDto.getId());

        assertEquals(truckDto.getRegistrationNumber(), foundTruck.getRegistrationNumber());
        assertEquals(truckDto.getModel(), foundTruck.getModel());
        assertEquals(truckDto.getCapacity(), foundTruck.getCapacity());
    }

    @Test
    public void findById_TruckNotExists_ExceptionThrows() {
        Long truckId = 10L;
        Mockito
                .when(truckRepository.findById(truckId))
                .thenReturn(Optional.empty());

        TruckNotFoundException thrown = assertThrows(TruckNotFoundException.class,
                () -> sut.findById(truckId));

        assertTrue(thrown.getMessage().contains("Truck with id " + truckId + " not found"));
    }

    @Test
    public void isRegistrationNumberExist_RegistrationNumberNotExist_True() {
        String registrationNumber = "HJ89017";
        Long truckId = 12L;

        Mockito
                .when(truckRepository.getTruckIdByRegistrationNumber(registrationNumber))
                .thenReturn(null);

        boolean result = sut.isRegistrationNumberExists(registrationNumber, truckId);

        assertTrue(result);
    }

    @Test
    public void isRegistrationNumberExist_SameTruck_True() {
        String registrationNumber = "HJ89017";
        Long truckId = 12L;

        Mockito
                .when(truckRepository.getTruckIdByRegistrationNumber(registrationNumber))
                .thenReturn(truckId);

        boolean result = sut.isRegistrationNumberExists(registrationNumber, truckId);

        assertTrue(result);
    }

    @Test
    public void isRegistrationNumberExist_RegistrationNumberExist_True() {
        String registrationNumber = "HJ89017";
        Long truckId = 12L;
        Long anotherTruckId = 13L;

        Mockito
                .when(truckRepository.getTruckIdByRegistrationNumber(registrationNumber))
                .thenReturn(anotherTruckId);

        boolean result = sut.isRegistrationNumberExists(registrationNumber, truckId);

        assertFalse(result);
    }

    @Test
    public void addTruck_SuitableTruck_True() {
        Mockito
                .when(truckRepository.getTruckIdByRegistrationNumber(truckDto.getRegistrationNumber()))
                .thenReturn(null);

        boolean result = sut.addTruck(truckDto);

        assertEquals(TruckCondition.SERVICEABLE, truckDto.getCondition());
        assertNull(truckDto.getId());
        assertTrue(result);
    }

    @Test
    public void failedAddTruckSameRegistrationNumbers() {
        Long truckId = 17L;
        Long existTruckId = 20L;
        truckDto.setId(truckId);

        Mockito
                .when(truckRepository.getTruckIdByRegistrationNumber(truckDto.getRegistrationNumber())).
                thenReturn(existTruckId);

        SavingTruckException thrown = assertThrows(SavingTruckException.class,
                () -> sut.addTruck(truckDto));

        assertTrue(thrown.getMessage()
                .contains("Truck with registration number " + truckDto.getRegistrationNumber() + " already exist"));
    }

    @Test
    public void canUpdateTruck_SuitableTruck_True() {
        Long truckId = 2L;
        truckDto.setId(truckId);

        Mockito
                .when(truckRepository.getTruckIdToUpdate(truckDto.getId()))
                .thenReturn(truckId);

        boolean result = sut.canUpdateTruck(truckDto.getId());

        assertTrue(result);
    }

    @Test
    public void canUpdateTruck_WrongTruck_True() {
        Long truckId = 2L;
        truckDto.setId(truckId);

        Mockito
                .when(truckRepository.getTruckIdToUpdate(truckDto.getId()))
                .thenReturn(null);

        boolean result = sut.canUpdateTruck(truckDto.getId());

        assertFalse(result);
    }

    @Test
    public void updateTruck_NewRegistrationNumber_True() {
        Long truckId = 23L;
        truckDto.setId(truckId);

        TruckDto sameTruck = new TruckDto();
        sameTruck.setId(23L);
        sameTruck.setRegistrationNumber("BB89009");
        sameTruck.setCondition(TruckCondition.SERVICEABLE);

        Mockito
                .when(truckRepository.getTruckIdToUpdate(truckDto.getId()))
                .thenReturn(truckDto.getId());
        Mockito
                .when(truckRepository.getTruckIdByRegistrationNumber(truckDto.getRegistrationNumber()))
                .thenReturn(null);
        Mockito
                .when(truckRepository.findById(23L))
                .thenReturn(Optional.of(truckMapper.fromDto(sameTruck)));

        boolean result = sut.updateTruck(truckDto);

        assertEquals(sameTruck.getCondition(), truckDto.getCondition());
        assertTrue(result);
    }

    @Test
    public void updateTruck_WrongTruck_ExceptionThrows() {
        Long truckId = 23L;
        truckDto.setId(truckId);

        Mockito
                .when(truckRepository.getTruckIdToUpdate(truckDto.getId()))
                .thenReturn(null);

        SavingTruckException thrown = assertThrows(SavingTruckException.class,
                () -> sut.updateTruck(truckDto));

        assertTrue(thrown.getMessage().contains(String.format(WRONG_TRUCK_OR_INCLUDED_IN_ORDER, truckDto.getId())));
    }

    @Test
    public void updateTruck_RegistrationNumberExist_ExceptionThrows() {
        Long truckId = 45L;
        Long existTruckId = 60L;
        truckDto.setId(truckId);


        Mockito
                .when(truckRepository.getTruckIdToUpdate(truckDto.getId()))
                .thenReturn(truckDto.getId());
        Mockito
                .when(truckRepository.getTruckIdByRegistrationNumber(truckDto.getRegistrationNumber()))
                .thenReturn(existTruckId);

        SavingTruckException thrown = assertThrows(SavingTruckException.class,
                () -> sut.updateTruck(truckDto));

        assertTrue(thrown.getMessage()
                .contains(String.format(REGISTRATION_NUMBER_EXISTS, truckDto.getRegistrationNumber())));
    }

    @Test
    public void setBrokenStatus_SuitableTruck_True() {
        Long truckId = 12L;
        TruckCondition condition = TruckCondition.SERVICEABLE;

        Mockito
                .when(truckRepository.getTruckToSetStatus(truckId, condition))
                .thenReturn(Optional.of(truckMapper.fromDto(truckDto)));

        boolean result = sut.setBrokenStatus(truckId);

        assertTrue(result);
    }

    @Test
    public void setBrokenStatus_WrongTruck_ExceptionThrows() {
        Long truckId = 12L;
        TruckCondition condition = TruckCondition.SERVICEABLE;

        Mockito
                .when(truckRepository.getTruckToSetStatus(truckId, condition))
                .thenReturn(Optional.empty());

        SetTruckConditionException thrown = assertThrows(SetTruckConditionException.class,
                () -> sut.setBrokenStatus(truckId));

        assertTrue(thrown.getMessage().contains(String.format(WRONG_TRUCK_OR_CONDITION, truckId)));
    }
}
