package com.freight.repositories;

import com.freight.models.Cargo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface CargoRepository extends CrudRepository<Cargo, Integer> {
}
