package com.example.services.serviceImpl;

import com.example.controller.exceptions.RegistrationNumberExistsException;
import com.example.controller.exceptions.TruckNotFoundException;
import com.example.database.models.Truck;
import com.example.database.models.commons.TruckCondition;
import com.example.database.repositories.TruckRepository;
import com.example.services.models.TruckDto;
import com.example.services.serviceImpl.validation.TruckValidator;
import com.example.services.TruckService;
import com.example.services.mappers.TruckMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
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
    public List<TruckDto> findAll() {
        List<TruckDto> truckDtos = new ArrayList<>();
        truckRepository.findAll().forEach(truck ->
                truckDtos.add(truckMapper.toDto(truck)));
        return truckDtos;
    }

    @Override
    public boolean updateTruck(TruckDto truckDto) {
        TruckValidator.validate(truckDto);
        Truck existTruck = truckRepository.getTruckByRegistrationNumber(truckDto.getRegistrationNumber());
        TruckDto sameTruck = findById(truckDto.getId());

        if (existTruck != null && !existTruck.getId().equals(truckDto.getId())) {
            throw new RegistrationNumberExistsException("Truck with registration number: " +
                    truckDto.getRegistrationNumber() + " already exists");
        }

        truckDto.setCondition(sameTruck.getCondition());
        truckRepository.save(truckMapper.fromDto(truckDto));
        return true;
    }

    @Override
    public boolean addTruck(TruckDto truckDto) {
        TruckValidator.validate(truckDto);
        Truck existTruck = truckRepository.getTruckByRegistrationNumber(truckDto.getRegistrationNumber());

        if (existTruck != null) {
            throw new RegistrationNumberExistsException("Truck with registration number: " +
                    truckDto.getRegistrationNumber() + " already exists");
        }

        truckDto.setId(null);
        truckDto.setCondition(TruckCondition.SERVICEABLE);
        truckRepository.save(truckMapper.fromDto(truckDto));
        return true;
    }

    @Override
    public List<TruckDto> getFreeTrucks(double weight) {
        return truckMapper.toListDto(truckRepository.getFreeTrucks(weight));
    }

    @Override
    public TruckDto getFreeTruck(Long truckId, double weight) {
        return truckMapper.toDto(truckRepository.getFreeTruck(truckId, weight));
    }
}
