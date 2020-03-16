package com.sproutt.eussyaeussyaapi.api.dto;

import com.sproutt.eussyaeussyaapi.api.dto.oauth2.OAuth2AttributeDto;
import java.io.Serializable;

public class SessionMember implements Serializable {

    private String name;

    public SessionMember(OAuth2AttributeDto oAuth2AttributeDto) {
        this.name = oAuth2AttributeDto.getName();
    }
}
