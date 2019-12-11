package com.example.services.models;

import com.example.services.models.interfaces.Searchable;

import javax.validation.Valid;

public class FullInfoManagerDto implements Searchable {
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

    @Override
    public void combineSearchString() {
        StringBuilder sb = new StringBuilder();

        sb
                .append(user.getFirstName()).append(" ")
                .append(user.getLastName()).append(" ")
                .append(user.getPhone()).append(" ")
                .append(user.getEmail());

        searchString = sb.toString().toLowerCase();
    }
}
