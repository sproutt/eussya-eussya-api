package com.sproutt.eussyaeussyaapi.api.config;

import com.sproutt.eussyaeussyaapi.api.security.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Value("${api.cors.allow-origins}")
    private String allowedOrigins;

    @Value("${jwt.accessTokenKey}")
    private String accessTokenKey;

    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/signUp/**", "/phrase/**", "/social/**", "/then/**", "/members/**", "/login/**", "/webjars/**", "/swagger-resources/**", "/v2/**", "/email-auth/**", "/swagger-ui.html");
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {

        return new MessageSourceAccessor(messageSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .exposedHeaders(accessTokenKey)
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .allowedOrigins(allowedOrigins.split(", "));
    }
}
