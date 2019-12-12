package com.example.services.serviceImpl;

import com.example.controller.exceptions.SavingCityException;
import com.example.database.models.City;
import com.example.database.repositories.CityRepository;
import com.example.services.CityService;
import com.example.services.mappers.CityMapper;
import com.example.services.models.CityDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CityServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityService sut = new CityServiceImpl();

    private CityDto cityDto;

    @Before
    public void setUp() {
        cityDto = new CityDto();
        cityDto.setCountry("Russia");
        cityDto.setName("Voronezh");
    }

    @Test
    public void addCity_SuitableCity_True() {
        Mockito
                .when(cityRepository.getCitiesByNameAndCountry(cityDto.getName(), cityDto.getCountry()))
                .thenReturn(Collections.emptyList());

        boolean result = sut.addCity(cityDto);

        assertTrue(result);
    }

    @Test
    public void addCity_ExistsCity_ExceptionThrown() {
        Mockito
                .when(cityRepository.getCitiesByNameAndCountry(cityDto.getName(), cityDto.getCountry()))
                .thenReturn(Collections.singletonList(new City()));

        SavingCityException thrown = assertThrows(SavingCityException.class,
                () -> sut.addCity(cityDto));

        assertTrue(thrown.getMessage().contains("City with name " + cityDto.getName() + " already exists"));
    }
}
