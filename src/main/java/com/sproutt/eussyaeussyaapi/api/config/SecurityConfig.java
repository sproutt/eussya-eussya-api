package com.sproutt.eussyaeussyaapi.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${api.cors.allow-origins}")
    private String allowedOrigins;

    @Value("${jwt.header}")
    private String tokenKey;

    @Autowired
    private AuthenticationSuccessHandler customOAuth2SuccessHandler;

    private static final String[] PUBLIC = {"/signUp/**", "/phrase/**", "/then/**", "/members/**", "/login/**", "/webjars/**", "/swagger-resources/**", "/v2/**", "/email-auth/**", "/swagger-ui.html"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                    .disable()
                .authorizeRequests()
                    .requestMatchers(CorsUtils::isPreFlightRequest)
                        .permitAll()
                    .antMatchers(PUBLIC)
                        .permitAll()
                    .anyRequest()
                        .authenticated()
                .and()
                .cors()
                .and()
                .oauth2Login()
                    .successHandler(customOAuth2SuccessHandler);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        Arrays.stream(allowedOrigins.split(", ")).forEach(configuration::addAllowedOrigin);
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader(tokenKey);
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
