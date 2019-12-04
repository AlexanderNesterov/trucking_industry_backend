package com.example.services.models;

import com.example.database.models.commons.ManagerStatus;

import javax.validation.Valid;

public class FullInfoManagerDto {
    private Long id;
    private ManagerStatus status;

    @Valid
    private FullInfoUserDto user;

    public FullInfoManagerDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ManagerStatus getStatus() {
        return status;
    }

    public void setStatus(ManagerStatus status) {
        this.status = status;
    }

    public FullInfoUserDto getUser() {
        return user;
    }

    public void setUser(FullInfoUserDto user) {
        this.user = user;
    }
}
