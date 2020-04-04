package com.sproutt.eussyaeussyaapi.object;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.FacebookOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GithubOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GoogleOAuth2UserDto;

public class DtoFactory {

    public static GithubOAuth2UserDto getGithubOAuth2UserDto() {
        GithubOAuth2UserDto defaultGithubDto = new GithubOAuth2UserDto();
        defaultGithubDto.setId("41421173");
        defaultGithubDto.setName("Byeongjae Jung");

        return defaultGithubDto;
    }

    public static FacebookOAuth2UserDto getFacebookOAuth2UserDto() {
        FacebookOAuth2UserDto defaultFacebookDto = new FacebookOAuth2UserDto();
        defaultFacebookDto.setId("2989263527824872");
        defaultFacebookDto.setName("정병재");

        return defaultFacebookDto;
    }

    public static GoogleOAuth2UserDto getGoogleOAuth2UserDto(){
        GoogleOAuth2UserDto defaultGoogleDto = new GoogleOAuth2UserDto();
        defaultGoogleDto.setId("105393180124787452232");
        defaultGoogleDto.setName("Byeongjae Jung");

        return defaultGoogleDto;
    }

}
