package com.sproutt.eussyaeussyaapi.acceptance;

import com.sproutt.eussyaeussyaapi.api.member.dto.JwtMemberDTO;
import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionDTO;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import com.sproutt.eussyaeussyaapi.domain.mission.MissionRepository;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MissionAcceptanceTest {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MissionRepository missionRepository;


    @Value("${jwt.header}")
    private String tokenKey;

    @Value("${jwt.secret}")
    private String secretKey;

    private String token;

    @BeforeEach
    void setUp() {
        Member member = MemberFactory.getDefaultMember();

        MissionDTO missionDTO = MissionDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime("2020-07-15T00:00:00.00Z")
                .build();

        Mission mission = new Mission(member, missionDTO);
        member.addMission(mission);

        memberRepository.saveAndFlush(member);
        missionRepository.saveAndFlush(mission);

        token = jwtHelper.createToken(secretKey, JwtMemberDTO.builder()
                                                             .id(member.getId())
                                                             .memberId(member.getMemberId())
                                                             .nickName(member.getNickName()).build());

        template.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                           .add(tokenKey, token);
                    request.getHeaders()
                           .setZonedDateTime("date", ZonedDateTime.of(2020, 7, 15, 5, 0, 0, 0, ZoneId.of("Asia/Seoul")));
                    return execution.execute(request, body);
                }));
    }

    @Test
    void name() {
        Mission mission = missionRepository.findAll().get(0);
        ResponseEntity response = template.getForEntity("/missions/" + mission.getId(), String.class);
        System.out.println("now: " + Instant.ofEpochMilli(response.getHeaders().getDate()).atZone(ZoneId.of("Asia/Seoul")).toLocalTime());
        System.out.println(response.getBody().toString().replace(",", ",\n"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println();

        response = template.exchange("/missions/" + mission.getId() + "/progress", HttpMethod.PUT, null, String.class);
        System.out.println("now: " + Instant.ofEpochMilli(response.getHeaders().getDate()).atZone(ZoneId.of("Asia/Seoul")).toLocalTime());
        System.out.println(response.getStatusCode());
        System.out.println();

        response = template.getForEntity("/missions/" + mission.getId(), String.class);
        System.out.println("now: " + Instant.ofEpochMilli(response.getHeaders().getDate()).atZone(ZoneId.of("Asia/Seoul")).toLocalTime());
        System.out.println(response.getBody().toString().replace(",", ",\n"));
        System.out.println();

        template.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                           .add(tokenKey, token);
                    request.getHeaders()
                           .setZonedDateTime("date", ZonedDateTime.of(2020, 7, 15, 5, 1, 0, 0, ZoneId.of("Asia/Seoul")));
                    return execution.execute(request, body);
                }));

        response = template.exchange("/missions/" + mission.getId() + "/pause", HttpMethod.PUT, null, String.class);
        System.out.println("now: " + Instant.ofEpochMilli(response.getHeaders().getDate()).atZone(ZoneId.of("Asia/Seoul")).toLocalTime());
        System.out.println(response.getStatusCode());
        System.out.println();

        response = template.getForEntity("/missions/" + mission.getId(), String.class);
        System.out.println("now: " + Instant.ofEpochMilli(response.getHeaders().getDate()).atZone(ZoneId.of("Asia/Seoul")).toLocalTime());
        System.out.println(response.getBody().toString().replace(",", ",\n"));
        System.out.println();
    }
}
