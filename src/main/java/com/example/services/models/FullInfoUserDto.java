package com.example.services.models;

import com.example.database.models.commons.AccountStatus;
import com.example.database.models.commons.Role;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import static com.example.services.serviceImpl.validation.Message.*;

/**
 * Class that uses only for adding new user
 */
public class FullInfoUserDto extends SimpleUserDto {

    @NotBlank(message = LOGIN + IS_BLANK)
    @Length(max = 32, message = LOGIN + TOO_LONG + LOGIN_MAX_LENGTH)
    private String login;

    @NotBlank(message = PASSWORD + IS_BLANK)
    @Length(min = 8, max = 32, message = PASSWORD + PASSWORD_LENGTH)
    private String password;

    private Role role;

    private AccountStatus status;

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

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
