package com.example.services.serviceImpl;

import com.example.controller.exceptions.DriverExistsException;
import com.example.controller.exceptions.DriverNotFoundException;
import com.example.database.models.Driver;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.Role;
import com.example.database.repositories.DriverRepository;
import com.example.services.models.FullInfoDriverDto;
import com.example.services.models.SimpleDriverDto;
import com.example.services.DriverService;
import com.example.services.mappers.DriverMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class DriverServiceImpl implements DriverService {

    private DriverRepository driverRepository;
    private DriverMapper driverMapper;

    public DriverServiceImpl(DriverMapper driverMapper) {
        this.driverMapper = driverMapper;
    }

    @Autowired
    public DriverServiceImpl(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    @Override
    public boolean isDriverLicenseExists(String driverLicense) {
        Long existDriverId = driverRepository.getDriverIdByDriverLicense(driverLicense);

        return existDriverId == null;
    }

    @Override
    public FullInfoDriverDto findById(Long driverDtoId) {
        Optional<Driver> driver = driverRepository.findById(driverDtoId);

        if (driver.isPresent()) {
            return driverMapper.toFullInfoDto(driver.get());
        } else {
            throw new DriverNotFoundException("Driver with id: " + driverDtoId + " not found");
        }
    }

    @Override
    public List<SimpleDriverDto> getDrivers(String text, int page, int pageSize) {
        Pageable request = PageRequest.of(page - 1, pageSize);
        return driverMapper.toListDto(this.driverRepository.getDrivers(text, request));
    }

    @Override
    public boolean updateDriver(@Valid FullInfoDriverDto driverDto) {
        FullInfoDriverDto sameDriverDto = findById(driverDto.getId());
        driverDto.getUser().setLogin(sameDriverDto.getUser().getLogin());

        checkSavingDriver(driverDto, true);

        driverDto.getUser().setRole(sameDriverDto.getUser().getRole());
        driverDto.getUser().setPassword(sameDriverDto.getUser().getPassword());
        driverDto.setStatus(sameDriverDto.getStatus());
        driverRepository.save(driverMapper.fromFullInfoDto(driverDto));
        return true;
    }

    @Override
    public boolean addDriver(@Valid FullInfoDriverDto driverDto) {
        checkSavingDriver(driverDto, false);

        driverDto.setId(null);
        driverDto.getUser().setId(null);
        driverDto.getUser().setRole(Role.DRIVER);
        driverDto.setStatus(DriverStatus.REST);
        driverDto.combineSearchString();
        driverRepository.save(driverMapper.fromFullInfoDto(driverDto));
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

    private Driver getDriverByLogin(String driverLogin) {
        return driverRepository.getDriverByLogin(driverLogin);
    }

    private Driver getDriverByDriverLicense(String driverLicense) {
        return driverRepository.getDriverByDriverLicense(driverLicense);
    }

    private void checkSavingDriver(FullInfoDriverDto savingDriver, boolean isUpdate) {
        StringBuilder exception = new StringBuilder();

        if (isUpdate && savingDriver.getUser().getId() == 0) {
            exception.append("User id cannot be equals or less than 0: ");
            exception.append(savingDriver.getUser().getId());
            throw new DriverExistsException(exception.toString());
        }

        if (!isUpdate) {
            Driver existDriverLogin = getDriverByLogin(savingDriver.getUser().getLogin());
            if (existDriverLogin != null) {
                exception.append("Driver with login: ");
                exception.append(savingDriver.getUser().getLogin());
                exception.append(" already exist");
                throw new DriverExistsException(exception.toString());
            }
        }

        Driver existDriverDriverLicense = getDriverByDriverLicense(savingDriver.getDriverLicense());
        if (existDriverDriverLicense == null ||
                (isUpdate && existDriverDriverLicense.getId().equals(savingDriver.getId()))) {
            return;
        }

        exception.append("Driver with driver license: ");
        exception.append(savingDriver.getDriverLicense());
        exception.append(" already exist");
        throw new DriverExistsException(exception.toString());
    }
}
