package com.example.services.serviceImpl;

import com.example.controller.exceptions.ManagerExistException;
import com.example.controller.exceptions.ManagerNotFoundException;
import com.example.controller.exceptions.SavingManagerException;
import com.example.database.models.Manager;
import com.example.database.models.commons.AccountStatus;
import com.example.database.models.commons.Role;
import com.example.database.repositories.ManagerRepository;
import com.example.services.ManagerService;
import com.example.services.mappers.ManagerMapper;
import com.example.services.models.FullInfoManagerDto;
import com.example.services.models.SimpleManagerDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public List<SimpleManagerDto> getManagers(String text, int page, int size) {
        Pageable request = PageRequest.of(page - 1, size);
        return managerMapper.toListDto(managerRepository.getManagers(text, request));
    }

    @Override
    public boolean updateManager(@Valid SimpleManagerDto manager) {
        Optional<Manager> sameManagerOpt = managerRepository.findById(manager.getId());
        Manager sameManager;

        if (sameManagerOpt.isEmpty()) {
            throw new SavingManagerException("Wrong manager id");
        } else {
            sameManager = sameManagerOpt.get();
        }

        sameManager.getUser().setFirstName(manager.getUser().getFirstName());
        sameManager.getUser().setLastName(manager.getUser().getLastName());
        sameManager.getUser().setPhone(manager.getUser().getPhone());
        sameManager.getUser().setEmail(manager.getUser().getEmail());
        managerRepository.save(sameManager);
/*        FullInfoManagerDto sameManager = findById(manager.getId());

        manager.getUser().setPassword(sameManager.getUser().getPassword());
        manager.getUser().setLogin(sameManager.getUser().getLogin());
        manager.getUser().setRole(Role.ADMIN);
        manager.getUser().setStatus(sameManager.getUser().getStatus());
        managerRepository.save(managerMapper.fromFullInfoDto(manager));*/
        return true;
    }

    @Override
    public boolean addManager(@Valid FullInfoManagerDto manager) {
        Manager managerWithSameLogin = managerRepository.getManagerByLogin(manager.getUser().getLogin());

        if (managerWithSameLogin != null) {
            throw new ManagerExistException("Manager with login: " + manager.getUser().getLogin() + "already exists");
        }

        manager.setId(null);
        manager.getUser().setRole(Role.ADMIN);
        manager.getUser().setStatus(AccountStatus.ACTIVE);
        manager.combineSearchString();
        managerRepository.save(managerMapper.fromFullInfoDto(manager));
        return true;
    }
}
