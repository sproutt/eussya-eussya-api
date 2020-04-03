package com.sproutt.eussyaeussyaapi.api.config;

import com.sproutt.eussyaeussyaapi.api.security.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Value("${api.cors.allow-origins}")
    private String[] allowedOrigins;

    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .excludePathPatterns("/*");
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {

        return new MessageSourceAccessor(messageSource);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .allowedOrigins(allowedOrigins);

    }
}
