package com.example.services.models;

import javax.validation.Valid;

/**
 * Class that uses for all purposes except adding managers
 */
public class SimpleManagerDto {
    private Long id;

    @Valid
    private SimpleUserDto user;

    public SimpleManagerDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SimpleUserDto getUser() {
        return user;
    }

    public void setUser(SimpleUserDto user) {
        this.user = user;
    }
}
