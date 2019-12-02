package com.example.services.serviceImpl;

import com.example.controller.exceptions.ManagerExistException;
import com.example.controller.exceptions.ManagerNotFoundException;
import com.example.database.models.User;
import com.example.database.models.commons.Role;
import com.example.database.repositories.ManagerRepository;
import com.example.services.mappers.FullInfoUserMapper;
import com.example.services.models.FullInfoUserDto;
import com.example.services.models.SimpleUserDto;
import com.example.services.serviceImpl.validation.UserValidator;
import com.example.services.ManagerService;
import com.example.services.mappers.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final UserMapper userMapper;
    private final FullInfoUserMapper fullInfoUserMapper;

    public ManagerServiceImpl(ManagerRepository managerRepository, UserMapper userMapper,
                              FullInfoUserMapper fullInfoUserMapper) {
        this.managerRepository = managerRepository;
        this.userMapper = userMapper;
        this.fullInfoUserMapper = fullInfoUserMapper;
    }

    @Override
    public FullInfoUserDto findById(Long managerId) {
        User user = managerRepository.findManagerByIdAndRole(managerId, Role.ADMIN);

        if (user != null) {
            return fullInfoUserMapper.toFullInfoDto(user);
        } else {
            throw new ManagerNotFoundException("Manager with id: " + managerId + "not found");
        }
    }

    @Override
    public List<SimpleUserDto> findAll() {
        return userMapper.toListDto(managerRepository.findAllManagersByRole(Role.ADMIN));
    }

    @Override
    public boolean updateManager(FullInfoUserDto manager) {
        UserValidator.validate(manager, false);
        FullInfoUserDto sameUser = findById(manager.getId());

        manager.setPassword(sameUser.getPassword());
        manager.setLogin(sameUser.getLogin());
        manager.setRole(Role.ADMIN);
        managerRepository.save(fullInfoUserMapper.fromFullInfoDto(manager));
        return true;
    }

    @Override
    public boolean addManager(FullInfoUserDto manager) {
        UserValidator.validate(manager, false);
        User userWithSameLogin = managerRepository.getManagerByLogin(manager.getLogin());

        if (userWithSameLogin != null) {
            throw new ManagerExistException("Manager with login: " + manager.getLogin() + "already exists");
        }

        manager.setId(null);
        manager.setRole(Role.ADMIN);
        managerRepository.save(fullInfoUserMapper.fromFullInfoDto(manager));
        return true;
    }
}
