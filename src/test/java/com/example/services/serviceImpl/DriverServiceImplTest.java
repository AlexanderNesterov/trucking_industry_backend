package com.example.services.serviceImpl;

import com.example.controller.exceptions.DriverExistsException;
import com.example.controller.exceptions.DriverNotFoundException;
import com.example.database.models.Driver;
import com.example.database.models.User;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.Role;
import com.example.database.repositories.DriverRepository;
import com.example.services.commons.IPasswordEncryptor;
import com.example.services.mappers.UserMapper;
import com.example.services.models.FullInfoDriverDto;
import com.example.services.models.FullInfoUserDto;
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

    private IPasswordEncryptor encryptor = rawPassword -> rawPassword;

    private DriverMapper driverMapper = new DriverMapperImpl();
    private FullInfoDriverDto addingDriver, updatingDriver;

    @InjectMocks
    private DriverService driverService = new DriverServiceImpl(driverMapper);

    @Mock
    private DriverRepository driverRepository;


    @Before
    public void setUp() {
        driverMapper.setPasswordEncoder(encryptor);

        addingDriver = new FullInfoDriverDto();
        FullInfoUserDto user = new FullInfoUserDto();
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setPassword("password");
        user.setEmail("ivanov@yandex.ru");
        addingDriver.setUser(user);

        updatingDriver = new FullInfoDriverDto();
        FullInfoUserDto user1 = new FullInfoUserDto();
        user1.setFirstName("Petr");
        user1.setLastName("Petrov");
        user1.setPassword("password");
        user1.setEmail("petrov@yandex.ru");
        updatingDriver.setUser(user1);
    }

    @Test
    public void findByIdSuccessfully() {
        FullInfoDriverDto existDriver = new FullInfoDriverDto();
        existDriver.setId(30L);
        existDriver.setDriverLicense("0102030405");
        FullInfoUserDto userDto1 = new FullInfoUserDto();
        userDto1.setFirstName("Vasya");
        userDto1.setLogin("driver_1");
        userDto1.setPassword("password");
        existDriver.setUser(userDto1);

        Mockito
                .when(driverRepository.findById(30L))
                .thenReturn(Optional.of(driverMapper.fromFullInfoDto(existDriver)));

//        FullInfoDriverDto foundDriver = driverService.findById(30L);

//        assertEquals(existDriver.getDriverLicense(), foundDriver.getDriverLicense());
//        assertEquals(existDriver.getUser().getLogin(), foundDriver.getUser().getLogin());
//        assertEquals(existDriver.getUser().getFirstName(), foundDriver.getUser().getFirstName());
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

        assertNull(addingDriver.getId());
        assertNull(addingDriver.getUser().getId());
        assertEquals(Role.DRIVER, addingDriver.getUser().getRole());
        assertEquals(DriverStatus.REST, addingDriver.getStatus());
        assertTrue(result);
    }

    @Test
    public void failedAddDriverLoginExists() {
        addingDriver.setDriverLicense("0102030405");
        addingDriver.getUser().setLogin("driver_1");

        FullInfoDriverDto existDriver = new FullInfoDriverDto();
        existDriver.setDriverLicense("1020304050");
        FullInfoUserDto userDto2 = new FullInfoUserDto();
        userDto2.setLogin("driver_1");
        existDriver.setUser(userDto2);

        Mockito
                .when(driverRepository.getDriverByLogin(addingDriver.getUser().getLogin()))
                .thenReturn(driverMapper.fromFullInfoDto(existDriver));

        DriverExistsException thrown = assertThrows(DriverExistsException.class,
                () -> driverService.addDriver(addingDriver));

        assertTrue(thrown.getMessage()
                .contains("Driver with login: " + addingDriver.getUser().getLogin() + " already exist"));
    }

    @Test
    public void failedAddDriverDriverLicenseExists() {
        addingDriver.setDriverLicense("0102030405");
        addingDriver.getUser().setLogin("driver_1");

        FullInfoDriverDto existDriver = new FullInfoDriverDto();
        existDriver.setDriverLicense("0102030405");
        FullInfoUserDto userDto2 = new FullInfoUserDto();
        userDto2.setLogin("driver_2");
        existDriver.setUser(userDto2);

        Mockito
                .when(driverRepository.getDriverByLogin(addingDriver.getUser().getLogin()))
                .thenReturn(null);
        Mockito
                .when(driverRepository.getDriverByDriverLicense(addingDriver.getDriverLicense()))
                .thenReturn(driverMapper.fromFullInfoDto(existDriver));

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

        FullInfoDriverDto sameDriver = new FullInfoDriverDto();
        sameDriver.setId(10L);
        sameDriver.setDriverLicense("0102030405");
        sameDriver.setStatus(DriverStatus.ACTIVE);
        FullInfoUserDto userDto2 = new FullInfoUserDto();
        userDto2.setRole(Role.DRIVER);
        userDto2.setPassword("password");
        userDto2.setLogin("driver_1");
        sameDriver.setUser(userDto2);

        Mockito
                .when(driverRepository.findById(updatingDriver.getId()))
                .thenReturn(Optional.of(driverMapper.fromFullInfoDto(sameDriver)));
        Mockito
                .when(driverRepository.getDriverByDriverLicense(updatingDriver.getDriverLicense()))
                .thenReturn(null);

//        boolean result = driverService.updateDriver(updatingDriver);

        assertEquals(sameDriver.getStatus(), updatingDriver.getStatus());
        assertEquals(sameDriver.getUser().getRole(), updatingDriver.getUser().getRole());
        assertEquals(sameDriver.getUser().getPassword(), updatingDriver.getUser().getPassword());
        assertEquals(sameDriver.getUser().getLogin(), updatingDriver.getUser().getLogin());
//        assertTrue(result);
    }

    @Test
    public void updateDriverSameDriverLicenseSuccessfully() {
        updatingDriver.setId(10L);
        updatingDriver.setDriverLicense("1020304050");
        updatingDriver.getUser().setId(3L);
        updatingDriver.getUser().setLogin("driver_1");
        updatingDriver.getUser().setFirstName("Ivan");
        updatingDriver.getUser().setLastName("Ivanov");

        FullInfoDriverDto sameDriver = new FullInfoDriverDto();
        sameDriver.setId(10L);
        sameDriver.setDriverLicense("1020304050");
        sameDriver.setStatus(DriverStatus.ACTIVE);
        FullInfoUserDto userDto2 = new FullInfoUserDto();
        userDto2.setRole(Role.DRIVER);
        userDto2.setPassword("password");
        userDto2.setLogin("driver_1");
        sameDriver.setUser(userDto2);

        Mockito
                .when(driverRepository.findById(updatingDriver.getId()))
                .thenReturn(Optional.of(driverMapper.fromFullInfoDto(sameDriver)));
        Mockito
                .when(driverRepository.getDriverByDriverLicense(updatingDriver.getDriverLicense()))
                .thenReturn(driverMapper.fromFullInfoDto(sameDriver));

//        boolean result = driverService.updateDriver(updatingDriver);

        assertEquals(sameDriver.getStatus(), updatingDriver.getStatus());
        assertEquals(sameDriver.getUser().getRole(), updatingDriver.getUser().getRole());
        assertEquals(sameDriver.getUser().getPassword(), updatingDriver.getUser().getPassword());
        assertEquals(sameDriver.getUser().getLogin(), updatingDriver.getUser().getLogin());
//        assertTrue(result);
    }

    @Test
    public void failedUpdateDriverDriverLicenseExists() {
        updatingDriver.setId(10L);
        updatingDriver.setDriverLicense("1020304050");
        updatingDriver.getUser().setLogin("driver_1");
        updatingDriver.getUser().setId(3L);

        FullInfoDriverDto sameDriver = new FullInfoDriverDto();
        sameDriver.setId(10L);
        sameDriver.setDriverLicense("2030405060");
        FullInfoUserDto userDto3 = new FullInfoUserDto();
        userDto3.setLogin("driver_1");
        sameDriver.setUser(userDto3);

        FullInfoDriverDto existDriver = new FullInfoDriverDto();
        existDriver.setId(12L);
        existDriver.setDriverLicense("1020304050");
        FullInfoUserDto userDto2 = new FullInfoUserDto();
        userDto2.setLogin("driver_2");
        existDriver.setUser(userDto2);

        Mockito
                .when(driverRepository.findById(updatingDriver.getId()))
                .thenReturn(Optional.of(driverMapper.fromFullInfoDto(sameDriver)));
        Mockito
                .when(driverRepository.getDriverByDriverLicense(updatingDriver.getDriverLicense()))
                .thenReturn(driverMapper.fromFullInfoDto(existDriver));

//        DriverExistsException thrown = assertThrows(DriverExistsException.class,
//                () -> driverService.updateDriver(updatingDriver));

//        assertTrue(thrown.getMessage()
//                .contains("Driver with driver license: " + updatingDriver.getDriverLicense() + " already exist"));
    }
}
