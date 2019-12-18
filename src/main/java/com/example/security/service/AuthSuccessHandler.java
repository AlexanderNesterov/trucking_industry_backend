package com.example.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

@Service
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthSuccessHandler(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            final UserDetails userDetails = (UserDetails) principal;
            String token = tokenService.generateToken(userDetails.getUsername());

            try (Writer writer = httpServletResponse.getWriter()) {
                objectMapper.writeValue(writer, token);
            }

            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
