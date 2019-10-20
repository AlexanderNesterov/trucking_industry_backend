package com.freight.DAOImpl;

import com.freight.DAO.TruckDAO;
import com.freight.models.Truck;
import com.freight.repositories.TruckRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TruckDAOImpl implements TruckDAO {

    private final TruckRepository truckRepository;

    public TruckDAOImpl(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    @Override
    public Truck findById(int truckId) {
        Optional<Truck> truck = truckRepository.findById(truckId);
        return truck.get();
    }

    @Override
    public List<Truck> findAll() {
        ArrayList<Truck> trucks = new ArrayList<>();
        truckRepository.findAll().forEach(trucks::add);

        return trucks;
    }

    @Override
    public Truck updateTruck(Truck truck) {
        return truckRepository.save(truck);
    }

    @Override
    public void addTruck(Truck truck) {
        truck.setId(0);
        truckRepository.save(truck);
    }

    @Override
    public void deleteTruckById(int truckId) {
        truckRepository.deleteById(truckId);
    }

    @Override
    public List<Object> getSpecs(int truckId) {
        return truckRepository.getRegNumAndShiftSize(truckId);
    }
}
