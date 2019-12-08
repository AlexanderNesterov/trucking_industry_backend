package com.example.services.serviceImpl;

import com.example.database.repositories.UserRepository;
import com.example.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isLoginExists(String login) {
        Long existUserId = userRepository.getUserIdByLogin(login);

        return existUserId == null;
    }
}
