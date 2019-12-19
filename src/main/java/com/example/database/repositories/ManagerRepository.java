package com.example.database.repositories;

import com.example.database.models.Manager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    @Query("from Manager m where m.user.login = :login")
    Manager getManagerByLogin(@Param("login") String login);

    @Query("from Manager m where m.searchString like %:text%")
    List<Manager> getManagers(@Param("text") String text, Pageable pageable);
}
