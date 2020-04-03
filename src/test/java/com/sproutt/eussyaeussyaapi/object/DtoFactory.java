package com.sproutt.eussyaeussyaapi.object;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GithubOAuth2UserDto;

public class DtoFactory {

    public static GithubOAuth2UserDto getGithubOAuth2UserDto() {
        GithubOAuth2UserDto defaultGithubDto = new GithubOAuth2UserDto();
        defaultGithubDto.setId("41421173");
        defaultGithubDto.setName("Byeongjae Jung");

        return defaultGithubDto;
    }

}
