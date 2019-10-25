package com.example.database.DAO;

import com.example.database.models.User;
import java.util.List;

public interface UserDAO {

    User findById(int userId);
    List<User> findAll();
    User updateUser(User user);
    User addUser(User user);
}
