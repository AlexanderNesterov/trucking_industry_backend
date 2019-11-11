package com.example.serviceImpl;

import com.example.controllers.exceptions.DriverExistsException;
import com.example.controllers.exceptions.DriverNotFoundException;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.Role;
import com.example.database.repositories.DriverRepository;
import com.example.models.DriverDto;
import com.example.models.UserDto;
import com.example.services.DriverService;
import com.example.services.mappers.DriverMapper;
import com.example.services.mappers.DriverMapperImpl;
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

    private DriverMapper driverMapper = new DriverMapperImpl();

    @InjectMocks
    private DriverService driverService = new DriverServiceImpl(driverMapper);

    @Mock
    private DriverRepository driverRepository;

    @Test
    public void findByIdSuccessfully() {
        DriverDto existDriver = new DriverDto();
        existDriver.setId(30);
        existDriver.setDriverLicense("0102030405");
        UserDto userDto1 = new UserDto();
        userDto1.setFirstName("Vasya");
        userDto1.setLogin("driver_1");
        existDriver.setUserDto(userDto1);

        Mockito.when(driverRepository.findById(30)).thenReturn(Optional.of(driverMapper.fromDto(existDriver)));
        DriverDto foundDriver = driverService.findById(30);

        assertEquals(existDriver.getDriverLicense(), foundDriver.getDriverLicense());
        assertEquals(existDriver.getUserDto().getLogin(), foundDriver.getUserDto().getLogin());
        assertEquals(existDriver.getUserDto().getFirstName(), foundDriver.getUserDto().getFirstName());
    }

    @Test(expected = DriverNotFoundException.class)
    public void failedFindById() {
        Mockito.when(driverRepository.findById(9)).thenReturn(Optional.empty());
        driverService.findById(9);
    }

    @Test
    public void addDriverSuccessfully() {
        DriverDto savingDriver = new DriverDto();
        savingDriver.setDriverLicense("0102030405");
        UserDto userDto1 = new UserDto();
        userDto1.setLogin("driver_1");
        savingDriver.setUserDto(userDto1);

        Mockito
                .when(driverRepository.getDriverByLogin(savingDriver.getUserDto().getLogin()))
                .thenReturn(null);
        Mockito
                .when(driverRepository.getDriverByDriverLicense(savingDriver.getDriverLicense()))
                .thenReturn(null);

        boolean result = driverService.addDriver(savingDriver);

        assertEquals(0, savingDriver.getId());
        assertEquals(0, savingDriver.getUserDto().getId());
        assertEquals(Role.DRIVER, savingDriver.getUserDto().getRole());
        assertEquals(DriverStatus.REST, savingDriver.getStatus());
        assertTrue(result);
    }

    @Test(expected = DriverExistsException.class)
    public void failedAddDriverLoginExists() {
        DriverDto savingDriver = new DriverDto();
        savingDriver.setDriverLicense("0102030405");
        UserDto userDto1 = new UserDto();
        userDto1.setLogin("driver_1");
        savingDriver.setUserDto(userDto1);

        DriverDto existDriver = new DriverDto();
        existDriver.setDriverLicense("1020304050");
        UserDto userDto2 = new UserDto();
        userDto2.setLogin("driver_1");
        existDriver.setUserDto(userDto2);

        Mockito
                .when(driverRepository.getDriverByLogin(savingDriver.getUserDto().getLogin()))
                .thenReturn(driverMapper.fromDto(existDriver));

        driverService.addDriver(savingDriver);
    }

    @Test(expected = DriverExistsException.class)
    public void failedAddDriverDriverLicenseExists() {
        DriverDto savingDriver = new DriverDto();
        savingDriver.setDriverLicense("0102030405");
        UserDto userDto1 = new UserDto();
        userDto1.setLogin("driver_1");
        savingDriver.setUserDto(userDto1);

        DriverDto existDriver = new DriverDto();
        existDriver.setDriverLicense("0102030405");
        UserDto userDto2 = new UserDto();
        userDto2.setLogin("driver_2");
        existDriver.setUserDto(userDto2);

        Mockito
                .when(driverRepository.getDriverByLogin(savingDriver.getUserDto().getLogin()))
                .thenReturn(null);
        Mockito
                .when(driverRepository.getDriverByDriverLicense(savingDriver.getDriverLicense()))
                .thenReturn(driverMapper.fromDto(existDriver));


        driverService.addDriver(savingDriver);
    }

    @Test
    public void updateDriverSuccessfully() {
        DriverDto savingDriver = new DriverDto();
        savingDriver.setId(10);
        savingDriver.setDriverLicense("1020304050");
        UserDto userDto1 = new UserDto();
        userDto1.setLogin("driver_1");
        savingDriver.setUserDto(userDto1);

        DriverDto sameDriver = new DriverDto();
        sameDriver.setId(10);
        sameDriver.setDriverLicense("0102030405");
        sameDriver.setStatus(DriverStatus.ACTIVE);
        UserDto userDto2 = new UserDto();
        userDto2.setRole(Role.DRIVER);
        userDto2.setPassword("password");
        userDto2.setLogin("driver_1");
        sameDriver.setUserDto(userDto2);

        Mockito
                .when(driverRepository.findById(savingDriver.getId()))
                .thenReturn(Optional.of(driverMapper.fromDto(sameDriver)));
        Mockito
                .when(driverRepository.getDriverByDriverLicense(savingDriver.getDriverLicense()))
                .thenReturn(null);

        boolean result = driverService.updateDriver(savingDriver);

        assertEquals(sameDriver.getStatus(), savingDriver.getStatus());
        assertEquals(sameDriver.getUserDto().getRole(), savingDriver.getUserDto().getRole());
        assertEquals(sameDriver.getUserDto().getPassword(), savingDriver.getUserDto().getPassword());
        assertEquals(sameDriver.getUserDto().getLogin(), savingDriver.getUserDto().getLogin());
        assertTrue(result);
    }

    @Test
    public void updateDriverSameDriverLicenseSuccessfully() {
        DriverDto savingDriver = new DriverDto();
        savingDriver.setId(10);
        savingDriver.setDriverLicense("1020304050");
        UserDto userDto1 = new UserDto();
        userDto1.setLogin("driver_1");
        savingDriver.setUserDto(userDto1);

        DriverDto sameDriver = new DriverDto();
        sameDriver.setId(10);
        sameDriver.setDriverLicense("1020304050");
        sameDriver.setStatus(DriverStatus.ACTIVE);
        UserDto userDto2 = new UserDto();
        userDto2.setRole(Role.DRIVER);
        userDto2.setPassword("password");
        userDto2.setLogin("driver_1");
        sameDriver.setUserDto(userDto2);

        Mockito
                .when(driverRepository.findById(savingDriver.getId()))
                .thenReturn(Optional.of(driverMapper.fromDto(sameDriver)));
        Mockito
                .when(driverRepository.getDriverByDriverLicense(savingDriver.getDriverLicense()))
                .thenReturn(driverMapper.fromDto(sameDriver));

        boolean result = driverService.updateDriver(savingDriver);

        assertEquals(sameDriver.getStatus(), savingDriver.getStatus());
        assertEquals(sameDriver.getUserDto().getRole(), savingDriver.getUserDto().getRole());
        assertEquals(sameDriver.getUserDto().getPassword(), savingDriver.getUserDto().getPassword());
        assertEquals(sameDriver.getUserDto().getLogin(), savingDriver.getUserDto().getLogin());
        assertTrue(result);
    }

    @Test(expected = DriverExistsException.class)
    public void failedUpdateDriverDriverLicenseExists() {
        DriverDto savingDriver = new DriverDto();
        savingDriver.setId(10);
        savingDriver.setDriverLicense("1020304050");
        UserDto userDto1 = new UserDto();
        userDto1.setLogin("driver_1");
        savingDriver.setUserDto(userDto1);

        DriverDto sameDriver = new DriverDto();
        sameDriver.setId(10);
        sameDriver.setDriverLicense("2030405060");
        UserDto userDto3 = new UserDto();
        userDto3.setLogin("driver_1");
        sameDriver.setUserDto(userDto3);

        DriverDto existDriver = new DriverDto();
        existDriver.setId(12);
        existDriver.setDriverLicense("1020304050");
        UserDto userDto2 = new UserDto();
        userDto2.setLogin("driver_2");
        existDriver.setUserDto(userDto2);

        Mockito
                .when(driverRepository.findById(savingDriver.getId()))
                .thenReturn(Optional.of(driverMapper.fromDto(sameDriver)));
        Mockito
                .when(driverRepository.getDriverByDriverLicense(savingDriver.getDriverLicense()))
                .thenReturn(driverMapper.fromDto(existDriver));

        driverService.updateDriver(savingDriver);
    }
}
