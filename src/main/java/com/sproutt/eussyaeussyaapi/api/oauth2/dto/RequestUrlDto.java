package com.sproutt.eussyaeussyaapi.api.oauth2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestUrlDto {

    private String google;
    private String github;
    private String facebook;

    public boolean isGoogle(String provider) {
        return provider.equalsIgnoreCase("google");
    }

    public boolean isGithub(String provider) {
        return provider.equalsIgnoreCase("github");
    }

    public boolean isFacebook(String provider) {
        return provider.equalsIgnoreCase("facebook");
    }
}
