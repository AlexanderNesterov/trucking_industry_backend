package com.freight.repositories;

import com.freight.models.Point;
import org.springframework.data.repository.CrudRepository;

public interface PointRepository extends CrudRepository<Point, Integer> {
}
