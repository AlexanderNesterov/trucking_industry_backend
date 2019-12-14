package com.example.services.serviceImpl;

import com.example.controller.exceptions.SavingTruckException;
import com.example.controller.exceptions.SetTruckConditionException;
import com.example.controller.exceptions.TruckNotFoundException;
import com.example.database.models.Truck;
import com.example.database.models.commons.TruckCondition;
import com.example.database.repositories.TruckRepository;
import com.example.services.models.TruckDto;
import com.example.services.TruckService;
import com.example.services.mappers.TruckMapper;
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

import static com.example.services.commons.message.TruckExceptionMessage.*;

@Service
@Validated
public class TruckServiceImpl implements TruckService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TruckServiceImpl.class);

    private TruckRepository truckRepository;
    private TruckMapper truckMapper;

    public TruckServiceImpl(TruckMapper truckMapper) {
        this.truckMapper = truckMapper;
    }

    @Autowired
    public TruckServiceImpl(TruckRepository truckRepository, TruckMapper truckMapper) {
        this.truckRepository = truckRepository;
        this.truckMapper = truckMapper;
    }

    @Override
    public TruckDto findById(Long truckId) {
        Optional<Truck> optional = truckRepository.findById(truckId);

        if (optional.isPresent()) {
            LOGGER.info("Truck with id: {} returned", truckId);
            return truckMapper.toDto(optional.get());
        } else {
            LOGGER.warn(String.format(TRUCK_NOT_FOUND, truckId));
            throw new TruckNotFoundException(String.format(TRUCK_NOT_FOUND, truckId));
        }
    }

    @Override
    public boolean isRegistrationNumberExists(String registrationNumber, Long truckId) {
        Long existTruckId = truckRepository.getTruckIdByRegistrationNumber(registrationNumber);
        return existTruckId == null || existTruckId.equals(truckId);
    }

    @Override
    public List<TruckDto> getTrucks(String text, int page, int pageSize) {
        Pageable request = PageRequest.of(page - 1, pageSize);
        return truckMapper.toListDto(truckRepository.getTrucks(text, request));
    }

    @Override
    public boolean updateTruck(@Valid TruckDto truckDto) {
        if (!canUpdateTruck(truckDto.getId())) {
            LOGGER.warn(String.format(WRONG_TRUCK_OR_INCLUDED_IN_ORDER, truckDto.getId()));
            throw new SavingTruckException(String.format(WRONG_TRUCK_OR_INCLUDED_IN_ORDER, truckDto.getId()));
        }

        Long existTruckId = truckRepository.getTruckIdByRegistrationNumber(truckDto.getRegistrationNumber());

        if (existTruckId != null && !existTruckId.equals(truckDto.getId())) {
            LOGGER.warn(String.format(REGISTRATION_NUMBER_EXISTS, truckDto.getRegistrationNumber()));
            throw new SavingTruckException(String.format(REGISTRATION_NUMBER_EXISTS, truckDto.getRegistrationNumber()));
        }

        TruckDto sameTruck = findById(truckDto.getId());
        truckDto.setCondition(sameTruck.getCondition());

        Truck truck = truckMapper.fromDto(truckDto);
        truck.combineSearchString();
        truckRepository.save(truck);
        LOGGER.info("Truck with id: {} updated", truckDto.getId());
        return true;
    }

    @Override
    public boolean addTruck(@Valid TruckDto truckDto) {
        Long existTruckId = truckRepository.getTruckIdByRegistrationNumber(truckDto.getRegistrationNumber());

        if (existTruckId != null) {
            LOGGER.warn(String.format(REGISTRATION_NUMBER_EXISTS,
                    truckDto.getRegistrationNumber()));
            throw new SavingTruckException(String.format(REGISTRATION_NUMBER_EXISTS,
                    truckDto.getRegistrationNumber()));
        }

        truckDto.setId(null);
        truckDto.setCondition(TruckCondition.SERVICEABLE);
        Truck truck = truckMapper.fromDto(truckDto);
        truck.combineSearchString();
        truckRepository.save(truck);
        LOGGER.info("Truck with registration number: {} added", truckDto.getRegistrationNumber());
        return true;
    }

    @Override
    public List<TruckDto> getFreeTrucks(double weight, String text, int page, int pageSize) {
        Pageable request = PageRequest.of(page - 1, pageSize);
        return truckMapper.toListDto(truckRepository.getFreeTrucks(weight, text, request));
    }

    @Override
    public TruckDto getFreeTruck(Long truckId, Long orderId, double weight) {
        return truckMapper.toDto(truckRepository.getFreeTruck(truckId, orderId, weight));
    }

    @Override
    public boolean canUpdateTruck(Long truckId) {
        Long existTruckId = truckRepository.getTruckIdToUpdate(truckId);
        return existTruckId != null;
    }

    @Override
    public boolean setBrokenStatus(Long truckId) {
        Optional<Truck> existsTruckOpt = truckRepository.getTruckToSetStatus(truckId, TruckCondition.SERVICEABLE);
        Truck existsTruck;

        if (existsTruckOpt.isEmpty()) {
            LOGGER.warn(String.format(WRONG_TRUCK_OR_CONDITION, truckId));
            throw new SetTruckConditionException(String.format(WRONG_TRUCK_OR_CONDITION, truckId));
        } else {
            existsTruck = existsTruckOpt.get();
        }

        existsTruck.setCondition(TruckCondition.BROKEN);
        existsTruck.combineSearchString();
        truckRepository.save(existsTruck);
        LOGGER.info("Set broken status to truck with id: {}", truckId);
        return true;
    }

    @Override
    public boolean setServiceableStatus(Long truckId) {
        Optional<Truck> existsTruckOpt = truckRepository.getTruckToSetStatus(truckId, TruckCondition.BROKEN);
        Truck existsTruck;

        if (existsTruckOpt.isEmpty()) {
            LOGGER.warn(WRONG_TRUCK_OR_CONDITION);
            throw new SetTruckConditionException(WRONG_TRUCK_OR_CONDITION);
        } else {
            existsTruck = existsTruckOpt.get();
        }

        existsTruck.setCondition(TruckCondition.SERVICEABLE);
        existsTruck.combineSearchString();
        truckRepository.save(existsTruck);
        LOGGER.info("Set serviceable status to truck with id: {}", truckId);
        return true;
    }
}
