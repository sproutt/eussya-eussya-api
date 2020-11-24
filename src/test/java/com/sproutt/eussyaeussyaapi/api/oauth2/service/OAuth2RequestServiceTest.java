package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
class OAuth2RequestServiceTest {

    final String MOCK_TOKEN = "mock";
    final String WRONG_TOKEN = "WRONG";

    RestTemplate restTemplate = mock(RestTemplate.class);

    <T> ResponseEntity<T> getResponseEntity(T userDto) {
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}