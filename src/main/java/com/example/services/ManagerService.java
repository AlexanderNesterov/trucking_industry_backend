package com.example.services;

import com.example.services.models.FullInfoManagerDto;
import com.example.services.models.SimpleManagerDto;

import javax.validation.Valid;
import java.util.List;

public interface ManagerService {

    /**
     * @return {@link com.example.services.models.SimpleManagerDto}
     */
    SimpleManagerDto findById(Long managerId);

    /**
     * @param text text from search string
     * @param page number of page
     * @param pageSize size of page
     * @return list of {@link com.example.services.models.SimpleManagerDto}
     */
    List<SimpleManagerDto> getManagers(String text, int page, int pageSize);

    /**
     * @param user {@link com.example.services.models.SimpleManagerDto}
     * @return true if driver successfully updated
     */
    boolean updateManager(@Valid SimpleManagerDto user);

    /**
     * @param user {@link com.example.services.models.FullInfoManagerDto}
     * @return true if driver successfully added
     */
    boolean addManager(@Valid FullInfoManagerDto user);

    boolean blockAccount(Long userId, Long managerId);
}
