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
import java.util.stream.Collectors;

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
    public FullInfoDriverDto findById(Long driverDtoId) {
        Optional<Driver> driver = driverRepository.findById(driverDtoId);

        if (driver.isPresent()) {
            return driverMapper.toFullInfoDto(driver.get());
        } else {
            throw new DriverNotFoundException("Driver with id: " + driverDtoId + " not found");
        }
    }

    @Override
    public List<SimpleDriverDto> findAll(int page, int pageSize) {
        Pageable request = PageRequest.of(page - 1, pageSize);

        return driverRepository.findAll(request).stream()
                .map(driver -> driverMapper.toDto(driver))
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateDriver(@Valid FullInfoDriverDto driverDto) {
        FullInfoDriverDto sameDriverDto = findById(driverDto.getId());
        driverDto.getUser().setLogin(sameDriverDto.getUser().getLogin());

        checkSavingDriver(driverDto, true);

        driverDto.getUser().setRole(sameDriverDto.getUser().getRole());
        driverDto.getUser().setPassword(sameDriverDto.getUser().getPassword());
        driverDto.setStatus(sameDriverDto.getStatus());
//        driverRepository.save(driverMapper.fromFullInfoDto(driverDto));
        return true;
    }

    @Override
    public boolean addDriver(@Valid FullInfoDriverDto driverDto) {
//        DriverValidator.validate(driverDto, false);
        checkSavingDriver(driverDto, false);

        driverDto.setId(null);
        driverDto.getUser().setId(null);
        driverDto.getUser().setRole(Role.DRIVER);
        driverDto.setStatus(DriverStatus.REST);
        driverDto.setSearchString(combineSearchString(driverDto));
//        driverRepository.save(driverMapper.fromFullInfoDto(driverDto));
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
    public List<SimpleDriverDto> getDriversBySearch(String text) {
        return driverMapper.toListDto(this.driverRepository.getDriverBySearch(text));
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

    private String combineSearchString(FullInfoDriverDto driver) {
        StringBuilder sb = new StringBuilder();

        sb.append(driver.getUser().getFirstName());
        sb.append(" ");
        sb.append(driver.getUser().getLastName());
        sb.append(" ");
        sb.append(driver.getUser().getEmail());
        sb.append(" ");
        sb.append(driver.getUser().getPhone());
        sb.append(" ");
        sb.append(driver.getDriverLicense());
        sb.append(" ");
        sb.append(driver.getStatus());

        return sb.toString().toLowerCase();
    }
}
