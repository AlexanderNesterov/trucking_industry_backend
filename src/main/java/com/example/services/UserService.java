package com.example.services;

import com.example.database.models.commons.AccountStatus;
import com.example.services.models.ChangePasswordDto;

public interface UserService {

    boolean isLoginExists(String login);

    /**
     * @return true is successfully unlock account
     */
    boolean unlockAccount(Long userId);

    /**
     * @param passwordDto {@link com.example.services.models.ChangePasswordDto}
     * @return true if password successfully changed
     */
    boolean changePassword(ChangePasswordDto passwordDto);

    /**
     * Set status to user account
     * @return true if status successfully set
     */
    boolean setStatus(AccountStatus status, Long userId);

    /**
     * Check that user has status
     */
    void checkUser(Long userId, AccountStatus status);
}
