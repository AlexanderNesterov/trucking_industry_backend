package com.example.database.repositories;

import com.example.database.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByLogin(String login);

    @Query("select u.id from User u where u.login = :login")
    Long getUserIdByLogin(@Param("login") String login);
}
