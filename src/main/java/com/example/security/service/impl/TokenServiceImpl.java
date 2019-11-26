package com.example.security.service.impl;

import com.example.database.models.Driver;
import com.example.database.models.User;
import com.example.database.models.commons.Role;
import com.example.database.repositories.DriverRepository;
import com.example.database.repositories.UserRepository;
import com.example.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Key SIGNING_KEY;

    static {
        final SecureRandom secureRandom = new SecureRandom();

        final byte[] bytes = new byte[512 / 8];
        secureRandom.nextBytes(bytes);

        SIGNING_KEY = Keys.hmacShaKeyFor(bytes);
    }

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;

    public TokenServiceImpl(UserRepository userRepository, DriverRepository driverRepository) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    public Jws<Claims> parseJws(final String token) {
        return Jwts
                .parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token);
    }

    @Override
    public String generateToken(final String username) {
        final Instant now = Instant.now();
        var date = Date.from(now);

        String token = "";
        User user = userRepository.getUserByLogin(username);
        String userRole = user.getRole().name();

        if (userRole.equals(Role.DRIVER.name())) {
            Driver driver = driverRepository.getDriverByLogin(username);
            token = Jwts
                    .builder()
                    .signWith(SIGNING_KEY)
                    .setSubject(username)
                    .claim("role", userRole)
                    .claim("driverId", driver.getId())
                    .claim("userId", user.getId())
                    .setIssuedAt(date)
                    .setExpiration(Date.from(now.plus(Duration.ofMinutes(20))))
                    .compact();
        }

        if (userRole.equals(Role.ADMIN.name())) {
            token = Jwts
                    .builder()
                    .signWith(SIGNING_KEY)
                    .setSubject(username)
                    .claim("role", userRole)
                    .claim("userId", user.getId())
                    .setIssuedAt(date)
                    .setExpiration(Date.from(now.plus(Duration.ofMinutes(20))))
                    .compact();
        }

        if (userRole.isEmpty()) {
            throw new UsernameNotFoundException("User " + username + " already removed from the system");
        }

        return token;
    }
}
