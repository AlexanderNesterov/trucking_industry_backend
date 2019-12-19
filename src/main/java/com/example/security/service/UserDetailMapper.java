package com.example.security.service;

import com.example.database.models.User;
import com.example.security.models.AuthUserDetails;
import org.mapstruct.Mapper;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper(componentModel = "spring")
public interface UserDetailMapper {
    default UserDetails mapUser(User user) {
        return new AuthUserDetails(user.getLogin(), user.getPassword(), user.getRole(), user.getStatus());
    }
}
