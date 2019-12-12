package com.example.services.serviceImpl;

import com.example.controller.exceptions.ManagerNotFoundException;
import com.example.controller.exceptions.SavingManagerException;
import com.example.database.models.Manager;
import com.example.database.models.commons.AccountStatus;
import com.example.database.models.commons.Role;
import com.example.database.repositories.ManagerRepository;
import com.example.services.ManagerService;
import com.example.services.UserService;
import com.example.services.mappers.ManagerMapper;
import com.example.services.models.FullInfoManagerDto;
import com.example.services.models.SimpleManagerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.example.services.commons.message.ManagerExceptionMessage.MANAGER_NOT_FOUND;
import static com.example.services.commons.message.ManagerExceptionMessage.WRONG_MANAGER_ID;
import static com.example.services.commons.message.UserExceptionMessage.LOGIN_EXISTS;

@Service
@Validated
public class ManagerServiceImpl implements ManagerService {

    private ManagerRepository managerRepository;
    private ManagerMapper managerMapper;
    private UserService userService;

    public ManagerServiceImpl(ManagerMapper managerMapper) {
        this.managerMapper = managerMapper;
    }

    @Autowired
    public ManagerServiceImpl(ManagerRepository managerRepository, UserService userService,
                              ManagerMapper managerMapper) {
        this.managerRepository = managerRepository;
        this.managerMapper = managerMapper;
        this.userService = userService;
    }

    @Override
    public SimpleManagerDto findById(Long managerId) {
        Optional<Manager> manager = managerRepository.findById(managerId);

        if (manager.isPresent()) {
            return managerMapper.toDto(manager.get());
        } else {
            throw new ManagerNotFoundException(String.format(MANAGER_NOT_FOUND, managerId));
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
            throw new SavingManagerException(String.format(WRONG_MANAGER_ID, manager.getId()));
        } else {
            sameManager = sameManagerOpt.get();
        }

        sameManager.getUser().setFirstName(manager.getUser().getFirstName());
        sameManager.getUser().setLastName(manager.getUser().getLastName());
        sameManager.getUser().setPhone(manager.getUser().getPhone());
        sameManager.getUser().setEmail(manager.getUser().getEmail());
        managerRepository.save(sameManager);
        return true;
    }

    @Override
    public boolean addManager(@Valid FullInfoManagerDto manager) {
        boolean isLoginExists = userService.isLoginExists(manager.getUser().getLogin());

        if (isLoginExists) {
            throw new SavingManagerException(String.format(LOGIN_EXISTS, manager.getUser().getLogin()));
        }

        manager.setId(null);
        manager.getUser().setRole(Role.ADMIN);
        manager.getUser().setStatus(AccountStatus.ACTIVE);
        manager.combineSearchString();
        managerRepository.save(managerMapper.fromFullInfoDto(manager));
        return true;
    }
}
