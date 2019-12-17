package com.example.services.serviceImpl;

import com.example.database.models.User;
import com.example.database.repositories.UserRepository;
import com.example.services.DriverService;
import com.example.services.ManagerService;
import com.example.services.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DriverService driverService;

    @Mock
    private ManagerService managerService;

    @InjectMocks
    private UserService sut = new UserServiceImpl();

    @Test
    public void isLoginExist_LoginNotExist_True() {
        String login = "user_1";

        Mockito
                .when(userRepository.getUserByLogin(login))
                .thenReturn(null);

        boolean result = sut.isLoginExists(login);

        assertFalse(result);
    }

    @Test
    public void isLoginExist_LoginExist_False() {
        String login = "user_1";

        Mockito
                .when(userRepository.getUserByLogin(login))
                .thenReturn(new User());

        boolean result = sut.isLoginExists(login);

        assertTrue(result);
    }

/*    @Test
    public void blockDriverAccount_SuitableAccount_True() {
        Long userId = 12L;
        Long driverId = 14L;
        AccountStatus status = AccountStatus.ACTIVE;

        Mockito
                .when(userRepository.getUserByIdAndStatus(userId, status))
                .thenReturn(new User());

        Mockito
                .when(driverService.getFreeDriver(driverId))
                .thenReturn(new SimpleDriverDto());

        boolean result = sut.blockDriverAccount(userId, driverId);

        assertTrue(result);
    }

    @Test
    public void blockDriverAccount_WrongUser_ExceptionThrows() {
        Long userId = 12L;
        Long driverId = 14L;
        AccountStatus status = AccountStatus.ACTIVE;

        Mockito
                .when(userRepository.getUserByIdAndStatus(userId, status))
                .thenReturn(null);

        BlockAccountException thrown = assertThrows(BlockAccountException.class,
                () -> sut.blockDriverAccount(userId, driverId));

        assertTrue(thrown.getMessage().contains("Wrong user id " + userId + " or status"));
    }*/

    /*@Test
    public void blockDriverAccount_WrongDriver_ExceptionThrows() {
        Long userId = 12L;
        Long driverId = 14L;
        AccountStatus status = AccountStatus.ACTIVE;

        Mockito
                .when(userRepository.getUserByIdAndStatus(userId, status))
                .thenReturn(new User());
        Mockito
                .when(driverService.getFreeDriver(driverId))
                .thenReturn(null);

        BlockAccountException thrown = assertThrows(BlockAccountException.class,
                () -> sut.blockDriverAccount(userId, driverId));

        assertTrue(thrown.getMessage().contains("Wrong driver id " + driverId + " or driver has an order"));
    }

    @Test
    public void blockManagerAccount_SuitableAccount_True() {
        Long userId = 15L;
        Long managerId = 17L;
        AccountStatus status = AccountStatus.ACTIVE;

        Mockito
                .when(userRepository.getUserByIdAndStatus(userId, status))
                .thenReturn(new User());
        Mockito
                .when(managerService.findById(managerId))
                .thenReturn(new SimpleManagerDto());

        boolean result = sut.blockManagerAccount(userId, managerId);

        assertTrue(result);
    }

    @Test
    public void blockManagerAccount_WrongManager_True() {
        Long userId = 15L;
        Long managerId = 17L;
        AccountStatus status = AccountStatus.ACTIVE;

        Mockito
                .when(userRepository.getUserByIdAndStatus(userId, status))
                .thenReturn(new User());
        Mockito
                .when(managerService.findById(managerId))
                .thenReturn(null);

        BlockAccountException thrown = assertThrows(BlockAccountException.class,
                () -> sut.blockManagerAccount(userId, managerId));

        assertTrue(thrown.getMessage().contains("Wrong manager id " + managerId));
    }*/
}
