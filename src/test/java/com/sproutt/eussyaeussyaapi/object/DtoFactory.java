package com.sproutt.eussyaeussyaapi.object;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.FacebookOAuth2UserDTO;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GithubOAuth2UserDTO;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GoogleOAuth2UserDTO;

public class DtoFactory {

    public static GithubOAuth2UserDTO getGithubOAuth2UserDTO() {
        GithubOAuth2UserDTO defaultGithubDto = new GithubOAuth2UserDTO();
        defaultGithubDto.setId("41421173");
        defaultGithubDto.setName("Byeongjae Jung");

        return defaultGithubDto;
    }

    public static FacebookOAuth2UserDTO getFacebookOAuth2UserDTO() {
        FacebookOAuth2UserDTO defaultFacebookDto = new FacebookOAuth2UserDTO();
        defaultFacebookDto.setId("2989263527824872");
        defaultFacebookDto.setName("정병재");
        defaultFacebookDto.setEmail("jbj616@naver.com");

        return defaultFacebookDto;
    }

    public static GoogleOAuth2UserDTO getGoogleOAuth2UserDTO() {
        GoogleOAuth2UserDTO defaultGoogleDTO = new GoogleOAuth2UserDTO();
        defaultGoogleDTO.setId("105393180124787452232");
        defaultGoogleDTO.setName("Byeongjae Jung");
        defaultGoogleDTO.setEmail("jbj616@gmail.com");

        return defaultGoogleDTO;
    }

}
