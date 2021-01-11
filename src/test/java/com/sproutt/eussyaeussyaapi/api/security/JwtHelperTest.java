package com.sproutt.eussyaeussyaapi.api.security;

import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JwtHelper.class)
class JwtHelperTest {

    @Autowired
    JwtHelper jwtHelper;

    @Test
    @DisplayName("토큰 생성이 잘 되는지 테스트")
    void create() {
        String token = jwtHelper.createAccessToken(MemberFactory.getDefaultMember().toJwtInfo());

        assertThat(token == null).isFalse();
    }

    @Test
    @DisplayName("생성된 토큰 유효성 테스트")
    void decrypt() {
        String token = jwtHelper.createAccessToken(MemberFactory.getDefaultMember().toJwtInfo());

        assertThat(jwtHelper.isUsable(token)).isTrue();
        assertThat(jwtHelper.decryptToken(token).getNickName()).isEqualTo(MemberFactory.getDefaultMember().getNickName());
    }
}