package com.example.services.serviceImpl;

import com.example.controller.exceptions.DriverNotFoundException;
import com.example.controller.exceptions.SavingDriverException;
import com.example.database.models.Driver;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.Role;
import com.example.database.repositories.DriverRepository;
import com.example.services.UserService;
import com.example.services.commons.IPasswordEncryptor;
import com.example.services.models.FullInfoDriverDto;
import com.example.services.models.FullInfoUserDto;
import com.example.services.DriverService;
import com.example.services.mappers.DriverMapper;
import com.example.services.mappers.DriverMapperImpl;
import com.example.services.models.SimpleDriverDto;
import com.example.services.models.SimpleUserDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static com.example.services.commons.message.DriverExceptionMessage.*;
import static com.example.services.commons.message.UserExceptionMessage.LOGIN_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class DriverServiceImplTest {

    private IPasswordEncryptor encryptor = rawPassword -> rawPassword;

    private DriverMapper driverMapper = new DriverMapperImpl();
    private FullInfoDriverDto addingDriver;
    private SimpleDriverDto driverDto;

    @InjectMocks
    private DriverService sut = new DriverServiceImpl(driverMapper);

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private UserService userService;

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

        driverDto = new SimpleDriverDto();
        driverDto.setDriverLicense("1020304050");
        SimpleUserDto user1 = new SimpleUserDto();
        user1.setFirstName("Petr");
        user1.setLastName("Petrov");
        user1.setEmail("petrov@yandex.ru");
        driverDto.setUser(user1);
    }

    @Test
    public void isDriverLicenseExists_LicenseNotExists_True() {
        String driverLicense = "1020304050";
        Long driverId = 10L;

        Mockito
                .when(driverRepository.getDriverIdByDriverLicense(driverLicense))
                .thenReturn(null);

        boolean result = sut.isDriverLicenseExists(driverLicense, driverId);
        assertTrue(result);
    }

    @Test
    public void isDriverLicenseExists_SameDriver_True() {
        String driverLicense = "1020304050";
        Long driverId = 10L;

        Mockito
                .when(driverRepository.getDriverIdByDriverLicense(driverLicense))
                .thenReturn(driverId);

        boolean result = sut.isDriverLicenseExists(driverLicense, driverId);
        assertTrue(result);
    }

    @Test
    public void isDriverLicenseExists_LicenseExists_False() {
        String driverLicense = "1020304050";
        Long driverId = 10L;
        Long existsDriverId = 11L;

        Mockito
                .when(driverRepository.getDriverIdByDriverLicense(driverLicense))
                .thenReturn(existsDriverId);

        boolean result = sut.isDriverLicenseExists(driverLicense, driverId);
        assertFalse(result);
    }

    @Test
    public void findById_IdExists_Driver() {
        Long driverId = 12L;
        driverDto.setId(driverId);

        Mockito
                .when(driverRepository.findById(driverDto.getId()))
                .thenReturn(Optional.of(driverMapper.fromDto(driverDto)));

        SimpleDriverDto foundDriver = sut.findById(driverDto.getId());

        assertEquals(driverDto.getDriverLicense(), foundDriver.getDriverLicense());
        assertEquals(driverDto.getUser().getFirstName(), foundDriver.getUser().getFirstName());
    }

    @Test
    public void findById_DriverNotExist_ExceptionThrows() {
        Long driverId = 9L;

        Mockito
                .when(driverRepository.findById(driverId))
                .thenReturn(Optional.empty());

        DriverNotFoundException thrown = assertThrows(DriverNotFoundException.class,
                () -> sut.findById(driverId));

        assertTrue(thrown.getMessage().contains(String.format(DRIVER_NOT_FOUND, driverId)));
    }

    @Test
    public void updateDriver_NewDriverLicense_True() {
        Long driverId = 5L;

        driverDto.setId(driverId);
        Driver sameDriver = driverMapper.fromDto(driverDto);
        driverDto.setDriverLicense("1111111111");
        driverDto.getUser().setFirstName("Vasili");

        Mockito
                .when(driverRepository.findById(driverDto.getId()))
                .thenReturn(Optional.of(sameDriver));
        Mockito
                .when(driverRepository.getDriverIdByDriverLicense(driverDto.getDriverLicense()))
                .thenReturn(null);

        boolean result = sut.updateDriver(driverDto);

        assertEquals(driverDto.getDriverLicense(), sameDriver.getDriverLicense());
        assertEquals(driverDto.getUser().getFirstName(), sameDriver.getUser().getFirstName());
        assertTrue(result);
    }

    @Test
    public void updateDriver_NewName_True() {
        Long driverId = 5L;

        driverDto.setId(driverId);
        Driver sameDriver = driverMapper.fromDto(driverDto);
        driverDto.setDriverLicense("1111111111");
        driverDto.getUser().setFirstName("Maksim");

        Mockito
                .when(driverRepository.findById(driverDto.getId()))
                .thenReturn(Optional.of(sameDriver));
        Mockito
                .when(driverRepository.getDriverIdByDriverLicense(driverDto.getDriverLicense()))
                .thenReturn(sameDriver.getId());

        boolean result = sut.updateDriver(driverDto);

        assertEquals(driverDto.getDriverLicense(), sameDriver.getDriverLicense());
        assertEquals(driverDto.getUser().getFirstName(), sameDriver.getUser().getFirstName());
        assertTrue(result);
    }

    @Test
    public void updateDriver_DriverNotExist_ExceptionThrows() {
        Long driverId = 4L;
        driverDto.setId(driverId);

        Mockito
                .when(driverRepository.findById(driverDto.getId()))
                .thenReturn(Optional.empty());

        SavingDriverException thrown = assertThrows(SavingDriverException.class,
                () -> sut.updateDriver(driverDto));

        assertTrue(thrown.getMessage().contains(String.format(WRONG_DRIVER_ID, driverId)));
    }

    @Test
    public void updateDriver_DriverLicenseExist_ExceptionThrows() {
        Long driverId = 5L;
        Long existAnotherDriverId = 6L;

        driverDto.setId(driverId);
        Driver sameDriver = driverMapper.fromDto(driverDto);
        driverDto.getUser().setFirstName("Maksim");

        Mockito
                .when(driverRepository.findById(driverDto.getId()))
                .thenReturn(Optional.of(sameDriver));
        Mockito
                .when(driverRepository.getDriverIdByDriverLicense(driverDto.getDriverLicense()))
                .thenReturn(existAnotherDriverId);

        SavingDriverException thrown = assertThrows(SavingDriverException.class,
                () -> sut.updateDriver(driverDto));

        assertTrue(thrown.getMessage().contains(String.format(DRIVER_LICENSE_EXISTS, driverDto.getDriverLicense())));
    }

    @Test
    public void addDriver_SuitableDriver_True() {
        addingDriver.setDriverLicense("0102030405");
        addingDriver.getUser().setLogin("driver_1");

        Mockito
                .when(userService.isLoginExists(addingDriver.getUser().getLogin()))
                .thenReturn(false);
        Mockito
                .when(driverRepository.getDriverIdByDriverLicense(addingDriver.getDriverLicense()))
                .thenReturn(null);

        boolean result = sut.addDriver(addingDriver);

        assertNull(addingDriver.getId());
        assertNull(addingDriver.getUser().getId());
        assertEquals(Role.DRIVER, addingDriver.getUser().getRole());
        assertEquals(DriverStatus.REST, addingDriver.getStatus());
        assertTrue(result);
    }

    @Test
    public void addDriver_LoginExist_ExceptionThrows() {
        addingDriver.getUser().setLogin("driver_1");

        Mockito
                .when(userService.isLoginExists(addingDriver.getUser().getLogin()))
                .thenReturn(true);

        SavingDriverException thrown = assertThrows(SavingDriverException.class,
                () -> sut.addDriver(addingDriver));

        assertTrue(thrown.getMessage()
                .contains(String.format(LOGIN_EXISTS, addingDriver.getUser().getLogin())));
    }

    @Test
    public void addDriver_DriverLicenseExist_ExceptionThrows() {
        addingDriver.setDriverLicense("0102030405");
        addingDriver.getUser().setLogin("driver_1");

        Long existDriverId = 6L;

        Mockito
                .when(userService.isLoginExists(addingDriver.getUser().getLogin()))
                .thenReturn(false);
        Mockito
                .when(driverRepository.getDriverIdByDriverLicense(addingDriver.getDriverLicense()))
                .thenReturn(existDriverId);

        SavingDriverException thrown = assertThrows(SavingDriverException.class,
                () -> sut.addDriver(addingDriver));

        assertTrue(thrown.getMessage()
                .contains(String.format(DRIVER_LICENSE_EXISTS, addingDriver.getDriverLicense())));
    }
}
