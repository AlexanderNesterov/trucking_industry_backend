package com.example.services.serviceImpl;

import com.example.controller.exceptions.ManagerNotFoundException;
import com.example.controller.exceptions.SavingManagerException;
import com.example.database.models.Manager;
import com.example.database.models.commons.AccountStatus;
import com.example.database.models.commons.Role;
import com.example.database.repositories.ManagerRepository;
import com.example.services.ManagerService;
import com.example.services.UserService;
import com.example.services.commons.IPasswordEncryptor;
import com.example.services.mappers.ManagerMapper;
import com.example.services.mappers.ManagerMapperImpl;
import com.example.services.models.FullInfoManagerDto;
import com.example.services.models.FullInfoUserDto;
import com.example.services.models.SimpleManagerDto;
import com.example.services.models.SimpleUserDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ManagerServiceImplTest {

    private IPasswordEncryptor passwordEncryptor = rawPassword -> rawPassword;

    private ManagerMapper managerMapper = new ManagerMapperImpl();

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ManagerService sut = new ManagerServiceImpl(managerMapper);

    private FullInfoManagerDto addingManager;
    private SimpleManagerDto managerDto;

    @Before
    public void setUp() {
        managerMapper.setPasswordEncoder(passwordEncryptor);

        FullInfoUserDto user = new FullInfoUserDto();
        user.setLogin("manager_1");
        user.setPassword("password");
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setEmail("ivanov@mail.com");
        addingManager = new FullInfoManagerDto();
        addingManager.setUser(user);

        SimpleUserDto userDto = new SimpleUserDto();
        userDto.setFirstName("Petr");
        userDto.setLastName("Petrov");
        userDto.setPhone("89006663636");
        userDto.setEmail("petrov@yandex.ru");
        managerDto = new SimpleManagerDto();
        managerDto.setUser(userDto);
    }

    @Test
    public void findById_SuitableId_Manager() {
        Long mangerId = 3L;
        managerDto.setId(mangerId);

        Mockito
                .when(managerRepository.findById(managerDto.getId()))
                .thenReturn(Optional.of(managerMapper.fromDto(managerDto)));

        SimpleManagerDto foundManager = sut.findById(managerDto.getId());

        assertEquals(managerDto.getUser().getFirstName(), foundManager.getUser().getFirstName());
        assertEquals(managerDto.getUser().getLastName(), foundManager.getUser().getLastName());
    }

    @Test
    public void findById_ManagerNotExist_Manager() {
        Long mangerId = 3L;

        Mockito
                .when(managerRepository.findById(mangerId))
                .thenReturn(Optional.empty());

        ManagerNotFoundException thrown = assertThrows(ManagerNotFoundException.class,
                () -> sut.findById(mangerId));

        assertTrue(thrown.getMessage().contains("Manager with id " + mangerId + " not found"));
    }

    @Test
    public void updateManager_SuitableManager_True() {
        Long managerId = 10L;
        managerDto.setId(managerId);
        Manager sameManager = managerMapper.fromDto(managerDto);
        managerDto.getUser().setFirstName("Oleg");

        Mockito
                .when(managerRepository.findById(managerDto.getId()))
                .thenReturn(Optional.of(sameManager));

        boolean result = sut.updateManager(managerDto);

        assertEquals(managerDto.getUser().getFirstName(), sameManager.getUser().getFirstName());
        assertTrue(result);
    }

    @Test
    public void updateManager_ManagerNoExist_ExceptionThrows() {
        Long managerId = 10L;
        managerDto.setId(managerId);

        Mockito
                .when(managerRepository.findById(managerDto.getId()))
                .thenReturn(Optional.empty());

        SavingManagerException thrown = assertThrows(SavingManagerException.class,
                () -> sut.updateManager(managerDto));

        assertTrue(thrown.getMessage().contains("Wrong manager id " + managerDto.getId()));
    }

    @Test
    public void addManager_SuitableManager_True() {
        Mockito
                .when(userService.isLoginExists(addingManager.getUser().getLogin()))
                .thenReturn(false);

        boolean result = sut.addManager(addingManager);

        assertNull(addingManager.getId());
        assertEquals(Role.ADMIN, addingManager.getUser().getRole());
        assertEquals(AccountStatus.ACTIVE, addingManager.getUser().getStatus());
        assertTrue(addingManager.getSearchString().contains(addingManager.getUser().getFirstName().toLowerCase()));
        assertTrue(result);
    }

    @Test
    public void addManager_LoginExists_ExceptionThrows() {
        Mockito
                .when(userService.isLoginExists(addingManager.getUser().getLogin()))
                .thenReturn(true);

        SavingManagerException thrown = assertThrows(SavingManagerException.class,
                () -> sut.addManager(addingManager));

        assertTrue(thrown.getMessage().contains("User with login " + addingManager.getUser().getLogin() +
                " already exists"));
    }
}
