package com.example.services.models;

import com.example.database.models.commons.ManagerStatus;
import com.example.services.models.interfaces.Searchable;

import javax.validation.Valid;

public class FullInfoManagerDto implements Searchable {
    private Long id;
    private ManagerStatus status;

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

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    @Override
    public void combineSearchString() {
        StringBuilder sb = new StringBuilder();

        sb
                .append(user.getFirstName()).append(" ")
                .append(user.getLastName()).append(" ")
                .append(user.getPhone()).append(" ")
                .append(user.getEmail()).append(" ")
                .append(status);

        searchString = sb.toString().toLowerCase();
    }
}
