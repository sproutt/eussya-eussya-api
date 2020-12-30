package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.exception.UnSupportedOAuth2Exception;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class OAuth2RequestServiceFactory {

    private final Map<String, OAuth2RequestService> providerServices;

    @Autowired
    public OAuth2RequestServiceFactory(Map<String, OAuth2RequestService> providerServices) {
        this.providerServices = providerServices;
    }

    public OAuth2RequestService getOAuth2RequestService(String provider) {
        if (!providerServices.keySet().contains(provider)) {
            throw new UnSupportedOAuth2Exception();
        }

        return providerServices.get(provider);
    }
}
