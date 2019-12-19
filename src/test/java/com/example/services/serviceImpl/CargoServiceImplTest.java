package com.example.services.serviceImpl;

import com.example.controller.exceptions.ChangeOrderStatusException;
import com.example.database.models.Cargo;
import com.example.database.models.commons.CargoStatus;
import com.example.database.repositories.CargoRepository;
import com.example.services.CargoService;
import com.example.services.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static com.example.services.commons.message.CargoExceptionMessage.SET_STATUS_ERROR;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CargoServiceImplTest {

    @Mock
    private OrderService orderService;

    @Mock
    private CargoRepository cargoRepository;

    @InjectMocks
    private CargoService sut = new CargoServiceImpl();

    private Cargo existCargo;

    @Before
    public void setUp() {
        existCargo = new Cargo();
    }

    @Test
    public void setDeliverStatus_SuitableCargo_True() {
        Long driverId = 12L;
        Long orderId = 10L;
        Long cargoId = 7L;

        Mockito
                .when(cargoRepository.getCargoToDeliver(orderId, cargoId, driverId))
                .thenReturn(Optional.of(existCargo));

        boolean result = sut.setDeliverStatus(cargoId, orderId, driverId);

        assertEquals(CargoStatus.DELIVERED, existCargo.getStatus());
        assertTrue(result);
    }

    @Test
    public void setDeliverStatus_WrongParameters_ExceptionThrown() {
        Long driverId = 12L;
        Long orderId = 10L;
        Long cargoId = 7L;

        ChangeOrderStatusException thrown = assertThrows(ChangeOrderStatusException.class,
                () -> sut.setDeliverStatus(cargoId, driverId, orderId));

        assertTrue(thrown.getMessage().contains(SET_STATUS_ERROR));
    }
}
