package com.example.serviceImpl;

import com.example.database.models.Driver;
import com.example.database.models.User;
import com.example.database.repositories.DriverRepository;
import com.example.models.DriverDto;
import com.example.models.UserDto;
import com.example.services.DriverService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
class DriverServiceImplTest {

    @Autowired
    private DriverService driverService;

    @MockBean
    private DriverRepository driverRepository;
/*
    @BeforeEach
    public void setUp() {
        Driver bernd = new Driver();
        bernd.setId(1);
        bernd.setDriverLicense("1111111111");
        User user = new User();
        user.setId(2);
        user.setLogin("driver_1");
        user.setPassword("password");
        user.setFirstName("Bernd");
        user.setLastName("Leno");
        user.setEmail("leno@gmail.com");
        bernd.setUser(user);

        Driver hector = new Driver();
        hector.setId(2);
        hector.setDriverLicense("9274829097");
        user = new User();
        user.setId(3);
        user.setLogin("driver_2");
        user.setPassword("password");
        user.setFirstName("Hector");
        user.setLastName("Bellerin");
        user.setEmail("bellerin@mail.com");
        hector.setUser(user);

        Driver lucas = new Driver();
        lucas.setId(3);
        lucas.setDriverLicense("8351745309");
        user = new User();
        user.setId(3);
        user.setLogin("driver_3");
        user.setPassword("password");
        user.setFirstName("Lucas");
        user.setLastName("Torreira");
        user.setEmail("torreira@gmail.com");
        lucas.setUser(user);

        Mockito.doReturn(Optional.of(hector)).when(driverRepository).findById(2);
        Mockito.when(driverRepository.findAll()).thenReturn(Arrays.asList(bernd, hector, lucas));
    }*/

    @Test
    void addDriver() {
        DriverDto petr = new DriverDto();
        petr.setDriverLicense("6378282998");
        UserDto userDto = new UserDto();
        userDto.setLogin("driver_4");
        userDto.setPassword("password");
        userDto.setFirstName("Petr");
        userDto.setLastName("Cech");
        userDto.setEmail("cech@gmail.com");
        petr.setUserDto(userDto);

        driverService.addDriver(petr);

        Assert.assertEquals("DRIVER", petr.getUserDto().getRole().name());
        Assert.assertEquals("REST", petr.getStatus().name());
    }

/*    @Test
    void findById() {

        DriverDto found = driverService.findById(2);

        Assert.assertEquals("Hector", found.getUserDto().getFirstName());
    }*/

/*    @Test
    void findAll() {
        List<DriverDto> list = driverService.findAll();

        Assert.assertEquals(3, list.size());
    }*/
}
