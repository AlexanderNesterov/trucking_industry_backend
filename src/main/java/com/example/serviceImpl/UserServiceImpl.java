package com.example.serviceImpl;

import com.example.database.DAO.UserDAO;
import com.example.models.UserDto;
import com.example.services.UserService;
import com.example.services.mappers.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final UserMapper userMapper;

    public UserServiceImpl(UserDAO userDAO, UserMapper userMapper) {
        this.userDAO = userDAO;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto findById(int userDtoId) {
        return userMapper.toDto(userDAO.findById(userDtoId));
    }

    @Override
    public List<UserDto> findAll() {
        return userMapper.toListDto(userDAO.findAll());
    }

    @Override
    public UserDto updateUser(@Valid UserDto user) {
        return userMapper.toDto(userDAO.updateUser(userMapper.fromDto(user)));
    }

    @Override
    public UserDto addUser(@Valid UserDto user) {
        return userMapper.toDto(userDAO.addUser(userMapper.fromDto(user)));
    }
}
