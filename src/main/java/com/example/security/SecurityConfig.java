package com.example.security;

import com.example.security.filter.AuthFilter;
import com.example.security.filter.JwtAccessFilter;
import com.example.security.service.TokenService;
import com.example.security.service.impl.UserDetailServiceImpl;
import com.example.services.commons.IPasswordEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailServiceImpl userDetailService;
    private final AuthenticationSuccessHandler successHandler;
    private final TokenService tokenService;

    public SecurityConfig(UserDetailServiceImpl userDetailService, AuthenticationSuccessHandler successHandler,
                          TokenService tokenService) {
        this.userDetailService = userDetailService;
        this.successHandler = successHandler;
        this.tokenService = tokenService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        auth.authenticationProvider(provider);
        super.configure(auth);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder("", 185000, 512);
        encoder.setEncodeHashAsBase64(true);
        return encoder;
    }

    @Bean
    IPasswordEncryptor encryptor() {
        return passwordEncoder()::encode;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .antMatcher("/trucking-industry/**")
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new AuthFilter(successHandler, authenticationManager()))
                .addFilterBefore(new JwtAccessFilter(authenticationManager(), tokenService), AuthFilter.class);
    }
}
