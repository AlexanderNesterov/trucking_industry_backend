package com.example.services;

import com.example.database.models.commons.AccountStatus;
import com.example.services.models.ChangePasswordDto;

public interface UserService {
    boolean isLoginExists(String login);
//    boolean blockDriverAccount(Long userId, Long driverId);
//    boolean blockManagerAccount(Long userId, Long managerId);
    boolean unlockAccount(Long userId);
    boolean changePassword(ChangePasswordDto passwordDto);
    boolean setStatus(AccountStatus status, Long userId);
    void checkUser(Long userId, AccountStatus status);
}
