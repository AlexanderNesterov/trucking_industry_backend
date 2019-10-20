package com.freight.repositories.customImpl;

import com.freight.models.Truck;
import com.freight.repositories.custom.CustomTruck;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class CustomTruckImpl implements CustomTruck<Truck> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object> getRegNumAndShiftSize(int truckId) {
        Query query = entityManager.createQuery("select registrationNumber, capacity from Truck where id=:truckId");
        query.setParameter("truckId", truckId);

        return query.getResultList();
    }
}
