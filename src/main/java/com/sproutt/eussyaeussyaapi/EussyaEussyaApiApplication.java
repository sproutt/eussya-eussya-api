package com.sproutt.eussyaeussyaapi;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableEncryptableProperties
@SpringBootApplication
public class EussyaEussyaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EussyaEussyaApiApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}

