package com.example.database.repositories;

import com.example.models.Cargo;
import org.springframework.data.repository.CrudRepository;

public interface CargoRepository extends CrudRepository<Cargo, Integer> {
}