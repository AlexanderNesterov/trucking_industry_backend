package com.example.services.serviceImpl.validation;

import com.example.services.models.FullInfoUserDto;
import com.example.services.models.SimpleUserDto;
import com.example.services.serviceImpl.validation.exception.UserValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator {
    private static FullInfoUserDto checkingUser;

    public static void validate(FullInfoUserDto user, boolean isUpdate) {
        checkingUser = user;
        if (!isUpdate) {
            checkLogin();
            checkPassword();
        }

        checkFirstName();
        checkLastName();
        checkPhone();
        checkEmail();
        checkingUser = null;
    }

    private static void checkLogin() {
        String login = checkingUser.getLogin();

        if (login == null || login.length() == 0) {
            throw new UserValidationException("Login cannot be blank");
        }

        if (login.length() > 32) {
            throw new UserValidationException("Login field length cannot be greater than 32");
        }
    }

    private static void checkPassword() {
        String password = checkingUser.getPassword();

        if (password == null || password.length() == 0) {
            throw new UserValidationException("Password cannot be blank");
        }

        if (password.length() > 32) {
            throw new UserValidationException("Password field length cannot be greater than 32");
        }

        if (password.length() < 8) {
            throw new UserValidationException("Password field length cannot be less than 8");
        }
    }

    private static void checkFirstName() {
        String firstName = checkingUser.getFirstName();

        if (firstName == null || firstName.length() == 0) {
            throw new UserValidationException("First name cannot be blank");
        }

        if (firstName.length() > 32) {
            throw new UserValidationException("First name field length cannot be greater than 32");
        }

        Pattern pattern = Pattern.compile("[[A-Z]|[a-z]][[a-z]|\\s|[A-Z]]{1,31}");
        Matcher matcher = pattern.matcher(firstName);

        if (!matcher.matches()) {
            throw new UserValidationException("Incorrect first name. " +
                    "First name can consist only English letters and white spaces");
        }
    }

    private static void checkLastName() {
        String lastName = checkingUser.getLastName();

        if (lastName == null || lastName.length() == 0) {
            throw new UserValidationException("Last name cannot be blank");
        }

        if (lastName.length() > 32) {
            throw new UserValidationException("Last name field length cannot be greater than 32");
        }

        Pattern pattern = Pattern.compile("[[A-Z]|[a-z]][[a-z]|\\s|[A-Z]]{1,31}");
        Matcher matcher = pattern.matcher(lastName);

        if (!matcher.matches()) {
            throw new UserValidationException("Incorrect last name. " +
                    "Last name can consist only english letters and white spaces");
        }
    }

    private static void checkPhone() {
        String phone = checkingUser.getPhone();

        if (phone == null || phone.length() == 0) {
            return;
        }

        Pattern pattern = Pattern.compile("\\d{11}");
        Matcher matcher = pattern.matcher(phone);

        if (!matcher.matches()) {
            throw new UserValidationException("Phone field can consist only 11 digits");
        }
    }

    private static void checkEmail() {
        String email = checkingUser.getEmail();

        if (email == null || email.length() == 0) {
            throw new UserValidationException("Email field cannot be blank");
        }

        if (email.length() > 32) {
            throw new UserValidationException("Email field length cannot be greater than 32");
        }

        Pattern pattern = Pattern.compile("[.|\\w]+@[.|\\w]+");
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new UserValidationException("Incorrect email");
        }
    }
}
