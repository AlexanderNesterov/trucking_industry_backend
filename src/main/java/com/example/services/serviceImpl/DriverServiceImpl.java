package com.example.services.serviceImpl;

import com.example.controller.exceptions.BlockAccountException;
import com.example.controller.exceptions.DriverNotFoundException;
import com.example.controller.exceptions.SavingDriverException;
import com.example.database.models.Driver;
import com.example.database.models.commons.AccountStatus;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.Role;
import com.example.database.repositories.DriverRepository;
import com.example.services.UserService;
import com.example.services.models.FullInfoDriverDto;
import com.example.services.models.SimpleDriverDto;
import com.example.services.DriverService;
import com.example.services.mappers.DriverMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.example.services.commons.message.DriverExceptionMessage.*;
import static com.example.services.commons.message.UserExceptionMessage.LOGIN_EXISTS;
import static com.example.services.commons.message.UserExceptionMessage.WRONG_DRIVER_OR_HAS_ORDER;

@Service
@Validated
public class DriverServiceImpl implements DriverService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DriverServiceImpl.class);

    private DriverRepository driverRepository;
    private DriverMapper driverMapper;
    private UserService userService;

    public DriverServiceImpl(DriverMapper driverMapper) {
        this.driverMapper = driverMapper;
    }

    @Autowired
    public DriverServiceImpl(DriverRepository driverRepository, UserService userService,
                             DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
        this.userService = userService;
    }

    @Override
    public boolean isDriverLicenseExists(String driverLicense, Long driverId) {
        Long existDriverId = driverRepository.getDriverIdByDriverLicense(driverLicense);
        return existDriverId == null || existDriverId.equals(driverId);
    }

    @Override
    public SimpleDriverDto findById(Long driverId) {
        Optional<Driver> driver = driverRepository.findById(driverId);

        if (driver.isPresent()) {
            LOGGER.info("Driver with id {} returned", driverId);
            return driverMapper.toDto(driver.get());
        } else {
            LOGGER.warn(String.format(DRIVER_NOT_FOUND, driverId));
            throw new DriverNotFoundException(String.format(DRIVER_NOT_FOUND, driverId));
        }
    }

    @Override
    public List<SimpleDriverDto> getDrivers(String text, int page, int pageSize) {
        Pageable request = PageRequest.of(page - 1, pageSize);
        return driverMapper.toListDto(this.driverRepository.getDrivers(text, request));
    }

    @Override
    public boolean updateDriver(@Valid SimpleDriverDto driverDto) {
        Optional<Driver> sameDriverOpt = driverRepository.findById(driverDto.getId());
        Driver sameDriver;

        if (sameDriverOpt.isEmpty()) {
            LOGGER.warn(String.format(WRONG_DRIVER_ID, driverDto.getId()));
            throw new SavingDriverException(String.format(WRONG_DRIVER_ID, driverDto.getId()));
        } else {
            sameDriver = sameDriverOpt.get();
        }

        checkDriverLicense(driverDto.getId(), driverDto.getDriverLicense(), true);

        sameDriver.setDriverLicense(driverDto.getDriverLicense());
        sameDriver.getUser().setFirstName(driverDto.getUser().getFirstName());
        sameDriver.getUser().setLastName(driverDto.getUser().getLastName());
        sameDriver.getUser().setPhone(driverDto.getUser().getPhone());
        sameDriver.getUser().setEmail(driverDto.getUser().getEmail());
        sameDriver.combineSearchString();
        driverRepository.save(sameDriver);
        LOGGER.info("Driver with login {} updated", sameDriver.getUser().getLogin());
        return true;
    }

    @Override
    public boolean addDriver(@Valid FullInfoDriverDto driverDto) {
        checkAddingDriver(driverDto);

        driverDto.setId(null);
        driverDto.getUser().setId(null);
        driverDto.getUser().setRole(Role.DRIVER);
        driverDto.getUser().setStatus(AccountStatus.ACTIVE);
        driverDto.setStatus(DriverStatus.REST);

        Driver driver = driverMapper.fromFullInfoDto(driverDto);
        driver.combineSearchString();
        driverRepository.save(driver);
        LOGGER.info("Driver with login: {} added", driverDto.getUser().getLogin());
        return true;
    }

    @Override
    public List<SimpleDriverDto> getFreeDrivers() {
        return driverMapper.toListDto(driverRepository.getFreeDrivers());
    }

    @Override
    public SimpleDriverDto getFreeDriver(Long driverId) {
        return driverMapper.toDto(driverRepository.getFreeDriver(driverId));
    }

    @Override
    public void setDriverStatus(Long[] driverIds, DriverStatus status) {
        driverRepository.setDriverStatus(driverIds, status);
    }

    @Override
    public boolean blockAccount(Long userId, Long driverId) {
        userService.checkUser(userId, AccountStatus.ACTIVE);
        SimpleDriverDto existsDriver = getFreeDriver(driverId);

        if (existsDriver == null) {
            LOGGER.warn(String.format(WRONG_DRIVER_OR_HAS_ORDER, driverId));
            throw new BlockAccountException(String.format(WRONG_DRIVER_OR_HAS_ORDER, driverId));
        }

        userService.setStatus(AccountStatus.BLOCKED, userId);
        LOGGER.info("Driver account with id {} blocked", driverId);
        return true;
    }

    private void checkDriverLicense(Long savingDriverId, String driverLicense, boolean isUpdate) {
        Long existDriverId = driverRepository.getDriverIdByDriverLicense(driverLicense);
        if (existDriverId == null ||
                (isUpdate && existDriverId.equals(savingDriverId))) {
            return;
        }

        LOGGER.warn(String.format(DRIVER_LICENSE_EXISTS, driverLicense));
        throw new SavingDriverException(String.format(DRIVER_LICENSE_EXISTS, driverLicense));
    }

    private void checkAddingDriver(FullInfoDriverDto savingDriver) {
        boolean isLoginExists = userService.isLoginExists(savingDriver.getUser().getLogin());
        if (isLoginExists) {
            LOGGER.warn(String.format(LOGIN_EXISTS, savingDriver.getUser().getLogin()));
            throw new SavingDriverException(String.format(LOGIN_EXISTS, savingDriver.getUser().getLogin()));
        }

        checkDriverLicense(savingDriver.getId(), savingDriver.getDriverLicense(), false);
    }
}
