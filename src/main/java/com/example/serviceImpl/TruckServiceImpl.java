package com.example.serviceImpl;

import com.example.database.DAO.TruckDAO;
import com.example.database.models.Truck;
import com.example.models.TruckDto;
import com.example.services.TruckService;
import com.example.services.mappers.TruckMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class TruckServiceImpl implements TruckService {

    private final TruckDAO truckDAO;
    private final TruckMapper truckMapper;

    public TruckServiceImpl(TruckDAO truckDAO, TruckMapper truckMapper) {
        this.truckDAO = truckDAO;
        this.truckMapper = truckMapper;
    }

    @Override
    public TruckDto findById(int truckDtoId) {
        return truckMapper.toDto(truckDAO.findById(truckDtoId));
    }

    @Override
    public List<TruckDto> findAll() {
        List<Truck> trucks = truckDAO.findAll();

        System.out.println(trucks);

        return truckMapper.toListDto(trucks);
    }

    @Override
    public TruckDto updateTruck(@Valid TruckDto truckDto) {
        return truckMapper.toDto(truckDAO.updateTruck(truckMapper.fromDto(truckDto)));
    }

    @Override
    public void addTruck(@Valid TruckDto truckDto) {
        truckDAO.addTruck(truckMapper.fromDto(truckDto));
    }

    @Override
    public void deleteTruckById(int truckDtoId) {
        truckDAO.deleteTruckById(truckDtoId);
    }
}
