package com.example.services.serviceImpl;

import com.example.controller.exceptions.SavingTruckException;
import com.example.controller.exceptions.SetStatusException;
import com.example.controller.exceptions.TruckExistsException;
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
    public TruckDto findById(Long truckDtoId) {
        Optional<Truck> optional = truckRepository.findById(truckDtoId);

        if (optional.isPresent()) {
            return truckMapper.toDto(optional.get());
        } else {
            throw new TruckNotFoundException("Truck with id: " + truckDtoId + " not found");
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
            throw new SavingTruckException("Wrong truck id or truck included in order");
        }

        Truck existTruck = truckRepository.getTruckByRegistrationNumber(truckDto.getRegistrationNumber());
        TruckDto sameTruck = findById(truckDto.getId());

        if (existTruck != null && !existTruck.getId().equals(truckDto.getId())) {
            throw new TruckExistsException("Truck with registration number: " +
                    truckDto.getRegistrationNumber() + " already exists");
        }

        truckDto.setCondition(sameTruck.getCondition());
        truckRepository.save(truckMapper.fromDto(truckDto));
        return true;
    }

    @Override
    public boolean addTruck(@Valid TruckDto truckDto) {
        Truck existTruck = truckRepository.getTruckByRegistrationNumber(truckDto.getRegistrationNumber());

        if (existTruck != null) {
            throw new TruckExistsException("Truck with registration number: " +
                    truckDto.getRegistrationNumber() + " already exists");
        }

        truckDto.setId(null);
        truckDto.setCondition(TruckCondition.SERVICEABLE);
        truckDto.combineSearchString();
        truckRepository.save(truckMapper.fromDto(truckDto));
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
            throw new SetStatusException("Wrong truck id or condition");
        } else {
            existsTruck = existsTruckOpt.get();
        }

        existsTruck.setCondition(TruckCondition.BROKEN);
        truckRepository.save(existsTruck);
        return true;
    }

    @Override
    public boolean setServiceableStatus(Long truckId) {
        Optional<Truck> existsTruckOpt = truckRepository.getTruckToSetStatus(truckId, TruckCondition.BROKEN);
        Truck existsTruck;

        if (existsTruckOpt.isEmpty()) {
            throw new SetStatusException("Wrong truck id or condition");
        } else {
            existsTruck = existsTruckOpt.get();
        }

        existsTruck.setCondition(TruckCondition.SERVICEABLE);
        truckRepository.save(existsTruck);
        return true;
    }
}
