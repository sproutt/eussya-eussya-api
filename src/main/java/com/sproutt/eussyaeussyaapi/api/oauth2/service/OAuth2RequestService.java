package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.OAuth2UserInfoDTO;

public interface OAuth2RequestService {

    OAuth2UserInfoDTO getUserInfo(String accessToken);
}
