package com.sproutt.eussyaeussyaapi.api.config;

import com.sproutt.eussyaeussyaapi.api.security.JwtAuthenticationFilter;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.api.security.auth.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${api.cors.allow-origins}")
    private String allowedOrigins;

    private final OAuth2UserService customOAuth2UserService;
    private final AuthenticationSuccessHandler customOAuth2SuccessHandler;
    private final JwtHelper jwtHelper;

    public SecurityConfig(OAuth2UserService customOAuth2UserService, AuthenticationSuccessHandler customOAuth2SuccessHandler, JwtHelper jwtHelper) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
        this.jwtHelper = jwtHelper;
    }

    private static final String[] PUBLIC = {"/auth/**", "/oauth2/**", "/signUp/**", "/phrase/**", "/members/**", "/login/**", "/email-auth/**"};


    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/webjars/**", "/swagger-resources/**", "/v2/**", "/swagger-ui.html")
                .antMatchers("/error");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                .formLogin()
                    .disable()
                .httpBasic()
                    .disable()
                .cors()
                .and()

                .exceptionHandling()
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()

                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .authorizeRequests()
                    .requestMatchers(CorsUtils::isPreFlightRequest)
                        .permitAll()
                    .antMatchers(PUBLIC)
                        .permitAll()
                    .anyRequest()
                        .authenticated()
                .and()

                .oauth2Login()
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService)
                    .and()
                    .successHandler(customOAuth2SuccessHandler);
        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtHelper), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        Arrays.stream(allowedOrigins.split(", ")).forEach(configuration::addAllowedOrigin);
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader(JwtHelper.ACCESS_TOKEN_HEADER);
        configuration.addExposedHeader(JwtHelper.REFRESH_TOKEN_HEADER);
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
