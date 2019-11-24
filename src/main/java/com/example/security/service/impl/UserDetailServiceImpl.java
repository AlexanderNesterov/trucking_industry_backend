package com.example.security.service.impl;

import com.example.database.repositories.UserRepository;
import com.example.security.service.UserDetailMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserDetailMapper userDetailMapper;

    public UserDetailServiceImpl(UserRepository userRepository, UserDetailMapper userDetailMapper) {
        this.userRepository = userRepository;
        this.userDetailMapper = userDetailMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userDetailMapper.mapUser(userRepository.getUserByLogin(username));

        if (userDetails == null) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return userDetails;
    }
}
