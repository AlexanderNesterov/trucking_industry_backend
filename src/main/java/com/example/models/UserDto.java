package com.example.models;

import com.example.database.models.commons.Role;
import com.example.models.validation.annotation.TextField;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import static com.example.models.validation.MessageCode.*;

public class UserDto {

    private int id;

    @NotBlank(message = LOGIN + BLANK)
    @Length(max = 32, message = LOGIN + TOO_LONG)
    private String login;

    @NotBlank(message = PWD + BLANK)
    @Length(min = 8, message = PWD + TOO_SHORT)
    private String password;

    @TextField
    private String firstName;

    @TextField
    private String lastName;

    private String phone;

    @NotBlank(message = EMAIL + BLANK)
    @Email
    @Length(max = 32, message = EMAIL + TOO_LONG)
    private String email;

    @NotNull(message = ROLE + NULL)
    private Role role;

    public UserDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
