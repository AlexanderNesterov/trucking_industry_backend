package com.example.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public interface TokenService {

    Jws<Claims> parseJws(String token);

    String generateToken(String username);
}
