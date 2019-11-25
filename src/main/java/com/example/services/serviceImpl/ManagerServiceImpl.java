package com.example.services.serviceImpl;

import com.example.controller.exceptions.ManagerExistException;
import com.example.controller.exceptions.ManagerNotFoundException;
import com.example.database.models.User;
import com.example.database.models.commons.Role;
import com.example.database.repositories.ManagerRepository;
import com.example.services.models.UserDto;
import com.example.services.serviceImpl.validation.UserValidator;
import com.example.services.ManagerService;
import com.example.services.mappers.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final UserMapper userMapper;

    public ManagerServiceImpl(ManagerRepository managerRepository, UserMapper userMapper) {
        this.managerRepository = managerRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto findById(Long managerId) {
        User user = managerRepository.findManagerByIdAndRole(managerId, Role.ADMIN);

        if (user != null) {
            return userMapper.toDto(user);
        } else {
            throw new ManagerNotFoundException("Manager with id: " + managerId + "not found");
        }
    }

    @Override
    public List<UserDto> findAll() {
        return userMapper.toListDto(managerRepository.findAllManagersByRole(Role.ADMIN));
    }

    @Override
    public boolean updateManager(UserDto manager) {
        UserValidator.validate(manager, false);
        UserDto sameUser = findById(manager.getId());

        manager.setPassword(sameUser.getPassword());
        manager.setLogin(sameUser.getLogin());
        manager.setRole(Role.ADMIN);
        managerRepository.save(userMapper.fromDto(manager));
        return true;
    }

    @Override
    public boolean addManager(UserDto manager) {
        UserValidator.validate(manager, false);
        User userWithSameLogin = managerRepository.getManagerByLogin(manager.getLogin());

        if (userWithSameLogin != null) {
            throw new ManagerExistException("Manager with login: " + manager.getLogin() + "already exists");
        }

        manager.setId(null);
        manager.setRole(Role.ADMIN);
        managerRepository.save(userMapper.fromDto(manager));
        return true;
    }
}
