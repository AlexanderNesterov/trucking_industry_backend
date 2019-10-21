package com.example.repositories;

import com.example.models.Point;
import org.springframework.data.repository.CrudRepository;

public interface PointRepository extends CrudRepository<Point, Integer> {
}
