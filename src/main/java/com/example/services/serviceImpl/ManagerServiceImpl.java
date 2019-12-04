package com.example.services.serviceImpl;

import com.example.controller.exceptions.ManagerExistException;
import com.example.controller.exceptions.ManagerNotFoundException;
import com.example.database.models.Manager;
import com.example.database.models.commons.Role;
import com.example.database.repositories.ManagerRepository;
import com.example.services.ManagerService;
import com.example.services.mappers.ManagerMapper;
import com.example.services.models.FullInfoManagerDto;
import com.example.services.models.SimpleManagerDto;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final ManagerMapper managerMapper;

    public ManagerServiceImpl(ManagerRepository managerRepository, ManagerMapper managerMapper) {
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
    }

    @Override
    public FullInfoManagerDto findById(Long managerId) {
        Optional<Manager> manager = managerRepository.findById(managerId);

        if (manager.isPresent()) {
            return managerMapper.toFullInfoDto(manager.get());
        } else {
            throw new ManagerNotFoundException("Manager with id: " + managerId + "not found");
        }
    }

    @Override
    public List<SimpleManagerDto> findAll() {
        return managerMapper.toListDto(managerRepository.findAll());
    }

    @Override
    public boolean updateManager(@Valid FullInfoManagerDto manager) {
//        UserValidator.validate(manager, false);
        FullInfoManagerDto sameManager = findById(manager.getId());

        manager.getUser().setPassword(sameManager.getUser().getPassword());
        manager.getUser().setLogin(sameManager.getUser().getLogin());
        manager.getUser().setRole(Role.ADMIN);
        managerRepository.save(managerMapper.fromFullInfoDto(manager));
        return true;
    }

    @Override
    public boolean addManager(@Valid FullInfoManagerDto manager) {
//        UserValidator.validate(manager, false);
        Manager managerWithSameLogin = managerRepository.getManagerByLogin(manager.getUser().getLogin());

        if (managerWithSameLogin != null) {
            throw new ManagerExistException("Manager with login: " + manager.getUser().getLogin() + "already exists");
        }

        manager.setId(null);
        manager.getUser().setRole(Role.ADMIN);
        managerRepository.save(managerMapper.fromFullInfoDto(manager));
        return true;
    }
}
