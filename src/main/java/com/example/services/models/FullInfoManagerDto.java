package com.example.services.models;

import javax.validation.Valid;

/**
 * Class that uses only for adding managers
 */
public class FullInfoManagerDto {
    private Long id;

    @Valid
    private FullInfoUserDto user;

    private String searchString;

    public FullInfoManagerDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FullInfoUserDto getUser() {
        return user;
    }

    public void setUser(FullInfoUserDto user) {
        this.user = user;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
