package com.sproutt.eussyaeussyaapi.api.security;

import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class JwtHelperTest {

    private JwtHelper jwtHelper;

    @BeforeEach
    void setUp() {
        jwtHelper = new JwtHelper();
    }

    @Value("${jwt.secret}")
    private String secretKey;

    @Test
    @DisplayName("토큰 생성이 잘 되는지 테스트")
    void create() {
        String token = jwtHelper.createAccessToken(secretKey, MemberFactory.getDefaultMember().toJwtInfo());

        assertThat(token == null).isFalse();
    }

    @Test
    @DisplayName("생성된 토큰 유효성 테스트")
    void decrypt() {
        String token = jwtHelper.createAccessToken(secretKey, MemberFactory.getDefaultMember().toJwtInfo());

        assertThat(jwtHelper.isUsable(secretKey, token)).isTrue();
        assertThat(jwtHelper.decryptToken(secretKey, token).getNickName()).isEqualTo(MemberFactory.getDefaultMember().getNickName());
    }
}