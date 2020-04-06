package com.sproutt.eussyaeussyaapi.api.security;

import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
class JwtHelperTest {

    @Autowired
    private JwtHelper jwtHelper;

    @Test
    void create() {

        String token = jwtHelper.createToken(MemberFactory.getDefaultMember().toJwtInfo());

        log.info("token: ()", token);

        assertThat(token == null).isFalse();
    }
}