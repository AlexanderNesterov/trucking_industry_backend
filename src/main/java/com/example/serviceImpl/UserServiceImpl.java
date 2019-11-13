package com.example.serviceImpl;

import com.example.database.repositories.UserRepository;
import com.example.models.UserDto;
import com.example.services.UserService;
import com.example.services.mappers.UserMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto findById(int userDtoId) {
        return userMapper.toDto(userRepository.findById(userDtoId).get());
    }

    @Override
    public List<UserDto> findAll() {
        List<UserDto> userDtos = new ArrayList<>();
        userRepository.findAll().forEach(user -> userDtos.add(userMapper.toDto(user)));
        return userDtos;
    }

    @Override
    public UserDto updateUser(UserDto user) {
        return userMapper.toDto(userRepository.save(userMapper.fromDto(user)));
    }

    @Override
    public UserDto addUser(UserDto user) {
        return userMapper.toDto(userRepository.save(userMapper.fromDto(user)));
    }
}
