package com.freight.serviceImpl;

import com.freight.DAO.TruckDAO;
import com.freight.models.Truck;
import com.freight.services.TruckService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TruckServiceImpl implements TruckService {

    private final TruckDAO truckDAO;

    public TruckServiceImpl(TruckDAO truckDAO) {
        this.truckDAO = truckDAO;
    }

    @Override
    public Truck findById(int truckId) {
        return truckDAO.findById(truckId);
    }

    @Override
    public List<Truck> findAll() {
        return truckDAO.findAll();
    }

    @Override
    public Truck updateTruck(Truck truck) {
        return truckDAO.updateTruck(truck);
    }

    @Override
    public void addTruck(Truck truck) {
        truckDAO.addTruck(truck);
    }

    @Override
    public void deleteTruckById(int truckId) {
        truckDAO.deleteTruckById(truckId);
    }

    @Override
    public List<Object> getSpecs(int truckId) {
        return truckDAO.getSpecs(truckId);
    }
}
