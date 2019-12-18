package com.example.services.serviceImpl;

import com.example.controller.exceptions.BlockAccountException;
import com.example.controller.exceptions.ChangePasswordException;
import com.example.database.models.User;
import com.example.database.models.commons.AccountStatus;
import com.example.database.repositories.UserRepository;
import com.example.services.UserService;
import com.example.services.mappers.UserMapper;
import com.example.services.models.ChangePasswordDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.services.commons.message.UserExceptionMessage.INCORRECT_CURRENT_PASSWORD;
import static com.example.services.commons.message.UserExceptionMessage.WRONG_USER_OR_STATUS;

@Service
public class UserServiceImpl implements UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserMapper userMapper;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl() {
    }

    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isLoginExists(String login) {
        User existUser = userRepository.getUserByLogin(login);
        return existUser != null;
    }

    @Override
    public boolean unlockAccount(Long userId) {
        checkUser(userId, AccountStatus.BLOCKED);
        userRepository.setStatus(AccountStatus.ACTIVE, userId);
        return true;
    }

    @Override
    public boolean changePassword(ChangePasswordDto passwordDto) {
        User user = userRepository.getUserByLogin(passwordDto.getLogin());
        boolean isPasswordsMatches = passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPassword());

        if (!isPasswordsMatches) {
            LOGGER.warn(INCORRECT_CURRENT_PASSWORD);
            throw new ChangePasswordException(INCORRECT_CURRENT_PASSWORD);
        }

        user.setPassword(userMapper.mapPassword(passwordDto.getNewPassword()));
        userRepository.save(user);
        LOGGER.info("Set new password to user with login: {}", passwordDto.getLogin());
        return true;
    }

    @Override
    public void checkUser(Long userId, AccountStatus status) {
        User existsUser = userRepository.getUserByIdAndStatus(userId, status);

        if (existsUser == null) {
            LOGGER.warn(String.format(WRONG_USER_OR_STATUS, userId));
            throw new BlockAccountException(String.format(WRONG_USER_OR_STATUS, userId));
        }
    }

    @Override
    public boolean setStatus(AccountStatus status, Long userId) {
        userRepository.setStatus(status, userId);
        LOGGER.info("Set status: {} to user with id: {}", status, userId);
        return true;
    }
}
