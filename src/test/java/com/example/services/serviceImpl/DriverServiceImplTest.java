package com.example.services.serviceImpl;

import com.example.controller.exceptions.DriverExistsException;
import com.example.controller.exceptions.DriverNotFoundException;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.Role;
import com.example.database.repositories.DriverRepository;
import com.example.services.models.DriverDto;
import com.example.services.models.UserDto;
import com.example.services.DriverService;
import com.example.services.mappers.DriverMapper;
import com.example.services.mappers.DriverMapperImpl;
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
public class DriverServiceImplTest {

/*    @Spy
    private IPasswordEncryptor passwordEncryptor;*/

    private DriverMapper driverMapper = new DriverMapperImpl();
    private DriverDto addingDriver, updatingDriver;

    @InjectMocks
    private DriverService driverService = new DriverServiceImpl(driverMapper);

    @Mock
    private DriverRepository driverRepository;


    @Before
    public void setUp() {
//        driverMapper.setPasswordEncoder(passwordEncryptor);

        addingDriver = new DriverDto();
        UserDto user = new UserDto();
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setPassword("password");
        user.setEmail("ivanov@yandex.ru");
        addingDriver.setUser(user);

        updatingDriver = new DriverDto();
        UserDto user1 = new UserDto();
        user1.setFirstName("Petr");
        user1.setLastName("Petrov");
        user1.setPassword("password");
        user1.setEmail("petrov@yandex.ru");
        updatingDriver.setUser(user1);
    }

    @Test
    public void findByIdSuccessfully() {
        DriverDto existDriver = new DriverDto();
        existDriver.setId(30L);
        existDriver.setDriverLicense("0102030405");
        UserDto userDto1 = new UserDto();
        userDto1.setFirstName("Vasya");
        userDto1.setLogin("driver_1");
        userDto1.setPassword("password");
        existDriver.setUser(userDto1);

        Mockito
                .when(driverRepository.findById(30L))
                .thenReturn(Optional.of(driverMapper.fromDto(existDriver)));

        DriverDto foundDriver = driverService.findById(30L);

        assertEquals(existDriver.getDriverLicense(), foundDriver.getDriverLicense());
        assertEquals(existDriver.getUser().getLogin(), foundDriver.getUser().getLogin());
        assertEquals(existDriver.getUser().getFirstName(), foundDriver.getUser().getFirstName());
    }

    @Test
    public void failedFindById() {
        Mockito.when(driverRepository.findById(9L)).thenReturn(Optional.empty());
        DriverNotFoundException thrown = assertThrows(DriverNotFoundException.class,
                () -> driverService.findById(9L));

        assertTrue(thrown.getMessage().contains("Driver with id: " + 9 + " not found"));
    }

    @Test
    public void addDriverSuccessfully() {
        addingDriver.setDriverLicense("0102030405");
        addingDriver.getUser().setLogin("driver_1");

        Mockito
                .when(driverRepository.getDriverByLogin(addingDriver.getUser().getLogin()))
                .thenReturn(null);
        Mockito
                .when(driverRepository.getDriverByDriverLicense(addingDriver.getDriverLicense()))
                .thenReturn(null);

        boolean result = driverService.addDriver(addingDriver);

        assertEquals(0, addingDriver.getId());
        assertEquals(0, addingDriver.getUser().getId());
        assertEquals(Role.DRIVER, addingDriver.getUser().getRole());
        assertEquals(DriverStatus.REST, addingDriver.getStatus());
        assertTrue(result);
    }

    @Test
    public void failedAddDriverLoginExists() {
        addingDriver.setDriverLicense("0102030405");
        addingDriver.getUser().setLogin("driver_1");

        DriverDto existDriver = new DriverDto();
        existDriver.setDriverLicense("1020304050");
        UserDto userDto2 = new UserDto();
        userDto2.setLogin("driver_1");
        existDriver.setUser(userDto2);

        Mockito
                .when(driverRepository.getDriverByLogin(addingDriver.getUser().getLogin()))
                .thenReturn(driverMapper.fromDto(existDriver));

        DriverExistsException thrown = assertThrows(DriverExistsException.class,
                () -> driverService.addDriver(addingDriver));

        assertTrue(thrown.getMessage()
                .contains("Driver with login: " + addingDriver.getUser().getLogin() + " already exist"));
    }

    @Test
    public void failedAddDriverDriverLicenseExists() {
        addingDriver.setDriverLicense("0102030405");
        addingDriver.getUser().setLogin("driver_1");

        DriverDto existDriver = new DriverDto();
        existDriver.setDriverLicense("0102030405");
        UserDto userDto2 = new UserDto();
        userDto2.setLogin("driver_2");
        existDriver.setUser(userDto2);

        Mockito
                .when(driverRepository.getDriverByLogin(addingDriver.getUser().getLogin()))
                .thenReturn(null);
        Mockito
                .when(driverRepository.getDriverByDriverLicense(addingDriver.getDriverLicense()))
                .thenReturn(driverMapper.fromDto(existDriver));

        DriverExistsException thrown = assertThrows(DriverExistsException.class,
                () -> driverService.addDriver(addingDriver));

        assertTrue(thrown.getMessage()
                .contains("Driver with driver license: " + addingDriver.getDriverLicense() + " already exist"));
    }

    @Test
    public void updateDriverSuccessfully() {
        updatingDriver.setId(10L);
        updatingDriver.setDriverLicense("1020304050");
        updatingDriver.getUser().setId(3L);
        updatingDriver.getUser().setLogin("driver_1");

        DriverDto sameDriver = new DriverDto();
        sameDriver.setId(10L);
        sameDriver.setDriverLicense("0102030405");
        sameDriver.setStatus(DriverStatus.ACTIVE);
        UserDto userDto2 = new UserDto();
        userDto2.setRole(Role.DRIVER);
        userDto2.setPassword("password");
        userDto2.setLogin("driver_1");
        sameDriver.setUser(userDto2);

        Mockito
                .when(driverRepository.findById(updatingDriver.getId()))
                .thenReturn(Optional.of(driverMapper.fromDto(sameDriver)));
        Mockito
                .when(driverRepository.getDriverByDriverLicense(updatingDriver.getDriverLicense()))
                .thenReturn(null);

        boolean result = driverService.updateDriver(updatingDriver);

        assertEquals(sameDriver.getStatus(), updatingDriver.getStatus());
        assertEquals(sameDriver.getUser().getRole(), updatingDriver.getUser().getRole());
        assertEquals(sameDriver.getUser().getPassword(), updatingDriver.getUser().getPassword());
        assertEquals(sameDriver.getUser().getLogin(), updatingDriver.getUser().getLogin());
        assertTrue(result);
    }

    @Test
    public void updateDriverSameDriverLicenseSuccessfully() {
        updatingDriver.setId(10L);
        updatingDriver.setDriverLicense("1020304050");
        updatingDriver.getUser().setId(3L);
        updatingDriver.getUser().setLogin("driver_1");
        updatingDriver.getUser().setFirstName("Ivan");
        updatingDriver.getUser().setLastName("Ivanov");

        DriverDto sameDriver = new DriverDto();
        sameDriver.setId(10L);
        sameDriver.setDriverLicense("1020304050");
        sameDriver.setStatus(DriverStatus.ACTIVE);
        UserDto userDto2 = new UserDto();
        userDto2.setRole(Role.DRIVER);
        userDto2.setPassword("password");
        userDto2.setLogin("driver_1");
        sameDriver.setUser(userDto2);

        /*Mockito
                .when(passwordEncryptor.encrypt(sameDriver.getUser().getPassword()))
                .thenReturn("password");*/
        Mockito
                .when(driverRepository.findById(updatingDriver.getId()))
                .thenReturn(Optional.of(driverMapper.fromDto(sameDriver)));
        Mockito
                .when(driverRepository.getDriverByDriverLicense(updatingDriver.getDriverLicense()))
                .thenReturn(driverMapper.fromDto(sameDriver));

        boolean result = driverService.updateDriver(updatingDriver);

        assertEquals(sameDriver.getStatus(), updatingDriver.getStatus());
        assertEquals(sameDriver.getUser().getRole(), updatingDriver.getUser().getRole());
        assertEquals(sameDriver.getUser().getPassword(), updatingDriver.getUser().getPassword());
        assertEquals(sameDriver.getUser().getLogin(), updatingDriver.getUser().getLogin());
        assertTrue(result);
    }

    @Test
    public void failedUpdateDriverDriverLicenseExists() {
        updatingDriver.setId(10L);
        updatingDriver.setDriverLicense("1020304050");
        updatingDriver.getUser().setLogin("driver_1");
        updatingDriver.getUser().setId(3L);

        DriverDto sameDriver = new DriverDto();
        sameDriver.setId(10L);
        sameDriver.setDriverLicense("2030405060");
        UserDto userDto3 = new UserDto();
        userDto3.setLogin("driver_1");
        sameDriver.setUser(userDto3);

        DriverDto existDriver = new DriverDto();
        existDriver.setId(12L);
        existDriver.setDriverLicense("1020304050");
        UserDto userDto2 = new UserDto();
        userDto2.setLogin("driver_2");
        existDriver.setUser(userDto2);

        Mockito
                .when(driverRepository.findById(updatingDriver.getId()))
                .thenReturn(Optional.of(driverMapper.fromDto(sameDriver)));
        Mockito
                .when(driverRepository.getDriverByDriverLicense(updatingDriver.getDriverLicense()))
                .thenReturn(driverMapper.fromDto(existDriver));

        DriverExistsException thrown = assertThrows(DriverExistsException.class,
                () -> driverService.updateDriver(updatingDriver));

        assertTrue(thrown.getMessage()
                .contains("Driver with driver license: " + updatingDriver.getDriverLicense() + " already exist"));
    }
}
