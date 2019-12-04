package com.example.services.models;

import com.example.database.models.commons.ManagerStatus;

public class SimpleManagerDto {
    private Long id;
    private ManagerStatus status;
    private SimpleUserDto user;

    public SimpleManagerDto() {
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

    public SimpleUserDto getUser() {
        return user;
    }

    public void setUser(SimpleUserDto user) {
        this.user = user;
    }
}
