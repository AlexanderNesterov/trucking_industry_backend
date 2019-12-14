package com.example.services.serviceImpl;

import com.example.controller.exceptions.BlockAccountException;
import com.example.controller.exceptions.ManagerNotFoundException;
import com.example.controller.exceptions.SavingManagerException;
import com.example.database.models.Manager;
import com.example.database.models.commons.AccountStatus;
import com.example.database.models.commons.Role;
import com.example.database.repositories.ManagerRepository;
import com.example.services.ManagerService;
import com.example.services.UserService;
import com.example.services.commons.message.UserExceptionMessage;
import com.example.services.mappers.ManagerMapper;
import com.example.services.models.FullInfoManagerDto;
import com.example.services.models.SimpleManagerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(ManagerServiceImpl.class);

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
            LOGGER.info("Manager with id: {} returned", managerId);
            return managerMapper.toDto(manager.get());
        } else {
            LOGGER.warn(String.format(MANAGER_NOT_FOUND, managerId));
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
            LOGGER.warn(String.format(WRONG_MANAGER_ID, manager.getId()));
            throw new SavingManagerException(String.format(WRONG_MANAGER_ID, manager.getId()));
        } else {
            sameManager = sameManagerOpt.get();
        }

        sameManager.getUser().setFirstName(manager.getUser().getFirstName());
        sameManager.getUser().setLastName(manager.getUser().getLastName());
        sameManager.getUser().setPhone(manager.getUser().getPhone());
        sameManager.getUser().setEmail(manager.getUser().getEmail());
        sameManager.combineSearchString();
        managerRepository.save(sameManager);
        LOGGER.info("Manager with login: {} updated", sameManager.getUser().getLogin());
        return true;
    }

    @Override
    public boolean addManager(@Valid FullInfoManagerDto managerDto) {
        boolean isLoginExists = userService.isLoginExists(managerDto.getUser().getLogin());

        if (isLoginExists) {
            LOGGER.warn(String.format(LOGIN_EXISTS, managerDto.getUser().getLogin()));
            throw new SavingManagerException(String.format(LOGIN_EXISTS, managerDto.getUser().getLogin()));
        }

        managerDto.setId(null);
        managerDto.getUser().setRole(Role.ADMIN);
        managerDto.getUser().setStatus(AccountStatus.ACTIVE);

        Manager manager = managerMapper.fromFullInfoDto(managerDto);
        manager.combineSearchString();
        managerRepository.save(manager);
        LOGGER.info("Manager with login: {} added", managerDto.getUser().getLogin());
        return true;
    }

    @Override
    public boolean blockAccount(Long userId, Long managerId) {
        userService.checkUser(userId, AccountStatus.ACTIVE);
        SimpleManagerDto existsManager = findById(managerId);

        if (existsManager == null) {
            LOGGER.warn(String.format(UserExceptionMessage.WRONG_MANAGER_ID, managerId));
            throw new BlockAccountException(String.format(UserExceptionMessage.WRONG_MANAGER_ID, managerId));
        }

        userService.setStatus(AccountStatus.BLOCKED, userId);
        LOGGER.info("Manager account with id: {} blocked", managerId);
        return true;
    }
}
