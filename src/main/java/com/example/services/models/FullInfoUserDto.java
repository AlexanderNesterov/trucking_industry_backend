package com.example.services.models;

import com.example.database.models.commons.Role;

public class FullInfoUserDto extends SimpleUserDto {
    private String login;
    private String password;
    private Role role;

    public FullInfoUserDto() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
