package com.example.security;

import com.example.security.filter.AuthFilter;
import com.example.security.filter.JwtAccessFilter;
import com.example.security.service.TokenService;
import com.example.security.service.impl.UserDetailServiceImpl;
import com.example.services.commons.IPasswordEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
//@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true)
@Configuration
public class SecurityConfig {

/*    private final UserDetailServiceImpl userDetailService;
//    private final AuthenticationSuccessHandler successHandler;
//    private final TokenService tokenService;

    public SecurityConfig(UserDetailServiceImpl userDetailService, AuthenticationSuccessHandler successHandler,
                          TokenService tokenService) {
        this.userDetailService = userDetailService;
//        this.successHandler = successHandler;
//        this.tokenService = tokenService;*/
//    }

/*    @Override
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
    }*/

/*    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new AuthFilter(successHandler, authenticationManager()))
                .addFilterBefore(new JwtAccessFilter(authenticationManager(), tokenService), AuthFilter.class);
    }*/


    @Configuration
    @Order(4)
    public static class GlobalSecurityConfig extends WebSecurityConfigurerAdapter {
        private final UserDetailServiceImpl userDetailService;
        private final AuthenticationSuccessHandler successHandler;
        private final TokenService tokenService;

        public GlobalSecurityConfig(AuthenticationSuccessHandler successHandler, UserDetailServiceImpl userDetailService,
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
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .cors().disable()
                    .authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .addFilter(new AuthFilter(successHandler, authenticationManager()))
                    .addFilterBefore(new JwtAccessFilter(authenticationManager(), tokenService), AuthFilter.class);
        }
    }
    /*@Configuration
    @Order(2)
    public static class UIConfigurationAdapApiWebSecurityter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/webjars/**")
                    .authorizeRequests()
                    .anyRequest().permitAll();
        }
    }*/
    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api-docs/**")
                    .authorizeRequests()
                    .anyRequest().permitAll();
        }
    }

    @Configuration
    @Order(3)
    public static class UIConfigurationAdapApiWebSecurityter1 extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/swagger-ui.html")
                    .authorizeRequests()
                    .anyRequest().permitAll();
        }
    }
}
