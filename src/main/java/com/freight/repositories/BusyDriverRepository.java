package com.freight.repositories;

import com.freight.models.BusyDriver;
import org.springframework.data.repository.CrudRepository;

public interface BusyDriverRepository extends CrudRepository<BusyDriver, Integer> {
}
