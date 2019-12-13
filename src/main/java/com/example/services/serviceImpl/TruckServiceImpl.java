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
            return truckMapper.toDto(optional.get());
        } else {
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
            throw new SavingTruckException(String.format(WRONG_TRUCK_OR_INCLUDED_IN_ORDER, truckDto.getId()));
        }

        Long existTruckId = truckRepository.getTruckIdByRegistrationNumber(truckDto.getRegistrationNumber());

        if (existTruckId != null && !existTruckId.equals(truckDto.getId())) {
            throw new SavingTruckException(String.format(REGISTRATION_NUMBER_EXISTS, truckDto.getRegistrationNumber()));
        }

        TruckDto sameTruck = findById(truckDto.getId());
        truckDto.setCondition(sameTruck.getCondition());

        Truck truck = truckMapper.fromDto(truckDto);
        truck.combineSearchString();
        truckRepository.save(truck);
        return true;
    }

    @Override
    public boolean addTruck(@Valid TruckDto truckDto) {
        Long existTruckId = truckRepository.getTruckIdByRegistrationNumber(truckDto.getRegistrationNumber());

        if (existTruckId != null) {
            throw new SavingTruckException(String.format(REGISTRATION_NUMBER_EXISTS,
                    truckDto.getRegistrationNumber()));
        }

        truckDto.setId(null);
        truckDto.setCondition(TruckCondition.SERVICEABLE);
        Truck truck = truckMapper.fromDto(truckDto);
        truck.combineSearchString();
        truckRepository.save(truck);
        return true;
    }

    @Override
    public List<TruckDto> getFreeTrucks(double weight) {
        return truckMapper.toListDto(truckRepository.getFreeTrucks(weight));
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
            throw new SetTruckConditionException(String.format(WRONG_TRUCK_OR_CONDITION, truckId));
        } else {
            existsTruck = existsTruckOpt.get();
        }

        existsTruck.setCondition(TruckCondition.BROKEN);
        existsTruck.combineSearchString();
        truckRepository.save(existsTruck);
        return true;
    }

    @Override
    public boolean setServiceableStatus(Long truckId) {
        Optional<Truck> existsTruckOpt = truckRepository.getTruckToSetStatus(truckId, TruckCondition.BROKEN);
        Truck existsTruck;

        if (existsTruckOpt.isEmpty()) {
            throw new SetTruckConditionException("Wrong truck id or condition");
        } else {
            existsTruck = existsTruckOpt.get();
        }

        existsTruck.setCondition(TruckCondition.SERVICEABLE);
        existsTruck.combineSearchString();
        truckRepository.save(existsTruck);
        return true;
    }
}
