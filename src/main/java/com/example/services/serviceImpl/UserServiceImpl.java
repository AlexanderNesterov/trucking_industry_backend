package com.example.services.serviceImpl;

import com.example.controller.exceptions.BlockAccountException;
import com.example.controller.exceptions.ChangePasswordException;
import com.example.database.models.User;
import com.example.database.models.commons.AccountStatus;
import com.example.database.repositories.UserRepository;
import com.example.services.DriverService;
import com.example.services.ManagerService;
import com.example.services.UserService;
import com.example.services.mappers.UserMapper;
import com.example.services.models.FullInfoManagerDto;
import com.example.services.models.ChangePasswordDto;
import com.example.services.models.SimpleDriverDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final DriverService driverService;
    private final ManagerService managerService;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository,
                           DriverService driverService, ManagerService managerService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.driverService = driverService;
        this.managerService = managerService;
    }

    @Override
    public boolean isLoginExists(String login) {
        Long existUserId = userRepository.getUserIdByLogin(login);

        return existUserId == null;
    }

    @Override
    public boolean blockDriverAccount(Long userId, Long driverId) {
        checkUser(userId, AccountStatus.ACTIVE);
        SimpleDriverDto existsDriver = driverService.getFreeDriver(driverId);

        if (existsDriver == null) {
            throw new BlockAccountException("Wrong driver id or driver has an order");
        }

        userRepository.setStatus(AccountStatus.BLOCKED, userId);
        return true;
    }

    @Override
    public boolean blockManagerAccount(Long userId, Long managerId) {
        checkUser(userId, AccountStatus.ACTIVE);
        FullInfoManagerDto existsManager = managerService.findById(managerId);

        if (existsManager == null) {
            throw new BlockAccountException("Wrong manager id");
        }

        userRepository.setStatus(AccountStatus.BLOCKED, userId);
        return true;
    }

    @Override
    public boolean unlockAccount(Long userId) {
        checkUser(userId, AccountStatus.BLOCKED);

        userRepository.setStatus(AccountStatus.ACTIVE, userId);
        return true;
    }

    @Override
    public boolean changePassword(ChangePasswordDto passwordDto) {
        String encodedPassword = userMapper.mapPassword(passwordDto.getCurrentPassword());
        Optional<User> existUserOpt = userRepository.getUserByLoginAndPassword(passwordDto.getLogin(), encodedPassword);
        User existUser;

        if (existUserOpt.isEmpty()) {
            throw new ChangePasswordException("Wrong current password or login");
        } else {
            if (passwordDto.getNewPassword().length() < 8) {
                throw new ChangePasswordException("Incorrect new password");
            }
            existUser = existUserOpt.get();
        }

        existUser.setPassword(userMapper.mapPassword(passwordDto.getNewPassword()));
        userRepository.save(existUser);
        return true;
    }

    private void checkUser(Long userId, AccountStatus status) {
        User existsUser = userRepository.getUserByIdAndStatus(userId, status);

        if (existsUser == null) {
            throw new BlockAccountException("Wrong user id or wrong account status");
        }
    }
}
