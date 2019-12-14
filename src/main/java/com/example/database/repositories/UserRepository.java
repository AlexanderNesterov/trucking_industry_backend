package com.example.database.repositories;

import com.example.database.models.User;
import com.example.database.models.commons.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User getUserByLogin(String login);

    User getUserByIdAndStatus(Long id, AccountStatus status);

    @Transactional
    @Modifying
    @Query("update User u set u.status = :status where u.id = :userId")
    void setStatus(@Param("status") AccountStatus status, @Param("userId") Long userId);
}
