package com.example.database.repositories;

import com.example.database.models.Manager;
import com.example.database.models.User;
import com.example.database.models.commons.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    @Query("from Manager m where m.user.login = :login")
    Manager getManagerByLogin(@Param("login") String login);
}
