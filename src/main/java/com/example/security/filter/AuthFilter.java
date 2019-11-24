package com.example.security.filter;

import com.example.security.models.LoginInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper mapper = new ObjectMapper();

  public AuthFilter(AuthenticationSuccessHandler successHandler,
                    AuthenticationManager authManager) {
    super();
    this.setAllowSessionCreation(false);
    this.setAuthenticationManager(authManager);
    this.setFilterProcessesUrl("/login");
    this.setAuthenticationSuccessHandler(successHandler);
  }

  @Override
  public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
      throws AuthenticationException {
    try {
      LoginInfo user = mapper.readValue(request.getReader(), LoginInfo.class);
      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
    } catch (IOException e) {
      throw new BadCredentialsException("Failed to parse user login info");
    }
  }

}
