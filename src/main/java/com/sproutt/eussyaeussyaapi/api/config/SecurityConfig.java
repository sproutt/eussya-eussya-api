package com.sproutt.eussyaeussyaapi.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler customOAuth2SuccessHandler;

    private static final String[] PUBLIC = {"/signUp/**", "/phrase/**", "/then/**", "/members/**", "/login/**", "/webjars/**", "/swagger-resources/**", "/v2/**", "/email-auth/**", "/swagger-ui.html"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                    .disable()
                .authorizeRequests()
                    .antMatchers(PUBLIC)
                        .permitAll()
                    .anyRequest()
                        .authenticated()
                .and()
                .oauth2Login()
                    .successHandler(customOAuth2SuccessHandler);
    }
}
