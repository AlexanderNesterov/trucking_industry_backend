package com.example.security.filter;

import com.example.security.models.CustomPrincipal;
import com.example.security.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class JwtAccessFilter extends BasicAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAccessFilter.class);

    private static final String LOGIN_API = "/trucking-industry/login";
    private static final String AUTH = "Authorization";
    private static final String BEARER = "Bearer ";
    private final TokenService service;


    public JwtAccessFilter(final AuthenticationManager authenticationManager, final TokenService service) {
        super(authenticationManager);
        this.service = service;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain chain)
            throws IOException, ServletException {

        response.setHeader("Access-Control-Allow-Origin", "*");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT,DELETE");
            response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, " +
                    "Origin,Accept, X-Requested-With, Content-Type, " +
                    "Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String path = request.getContextPath();
        if ("/".equals(path)) {
            path = "";
        }

        String reqUri = request.getRequestURI();
        if ((path + LOGIN_API).equalsIgnoreCase(reqUri)) {
            chain.doFilter(request, response);
            return;
        }

        String auth = request.getHeader(AUTH);
        if (auth == null || auth.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            LOGGER.debug("No authentication information provided to access {}", reqUri);
            return;
        }

        if (!auth.startsWith(BEARER)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            LOGGER.debug("No Bearer token provided to access {}", reqUri);
            return;
        }

        String jwt = auth.substring(BEARER.length());
        Authentication authentication;
        try {
            Jws<Claims> jwtToken = service.parseJws(jwt);

            if (jwtToken.getBody().containsKey("refresh")) {
                throw new BadCredentialsException("No refresh tokens allowed here!");
            }

            String role = jwtToken.getBody().get("role", String.class);
            Set<SimpleGrantedAuthority> authority = new HashSet<SimpleGrantedAuthority>();
            authority.add(new SimpleGrantedAuthority("ROLE_" + role));

            CustomPrincipal principal = new CustomPrincipal();
            principal.setSubject(jwtToken.getBody().getSubject());
            principal.setDriverId(jwtToken.getBody().get("driverId", Long.class));
            principal.setManagerId(jwtToken.getBody().get("managerId", Long.class));
            principal.setUserId(jwtToken.getBody().get("userId", Long.class));

            authentication
                    = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    authority);
        } catch (Exception e) {
            LOGGER.debug("Failed to parse token", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}
