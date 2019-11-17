package com.example.database.repositories;

import com.example.database.models.User;
import com.example.database.models.commons.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerRepository extends JpaRepository<User, Long> {
    User getManagerByLogin(String login);
    User findManagerByIdAndRole(Long id, Role role);
    List<User> findAllManagersByRole(Role role);
}
