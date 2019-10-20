package com.freight.repositories;

import com.freight.models.Cargo;
import org.springframework.data.repository.CrudRepository;

public interface CargoRepository extends CrudRepository<Cargo, Integer> {
}
