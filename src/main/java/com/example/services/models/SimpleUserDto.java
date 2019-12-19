package com.example.services.models;

import com.example.database.models.commons.AccountStatus;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.example.services.serviceImpl.validation.Message.*;

/**
 * Class that uses for all purposes except adding users
 */
public class SimpleUserDto {

    private Long id;

    @NotBlank(message = FIRST_NAME + IS_BLANK)
    @Pattern(regexp = "[[A-Z]|[a-z]][[a-z]|\\s|[A-Z]]{1,31}", message = FIRST_NAME + INVALID_FORMAT)
    private String firstName;

    @NotBlank(message = LAST_NAME + IS_BLANK)
    @Pattern(regexp = "[[A-Z]|[a-z]][[a-z]|\\s|[A-Z]]{1,31}", message = LAST_NAME + INVALID_FORMAT)
    private String lastName;

    @Pattern(regexp = "^$|\\d{11}", message = PHONE + INVALID_FORMAT)
    private String phone;

    @NotBlank(message = EMAIL + IS_BLANK)
    @Email(message = EMAIL + INVALID_FORMAT)
    private String email;

    private AccountStatus status;

    public SimpleUserDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
