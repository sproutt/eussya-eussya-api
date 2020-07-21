package com.sproutt.eussyaeussyaapi.acceptance;

import com.sproutt.eussyaeussyaapi.api.member.dto.JwtMemberDTO;
import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionDTO;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import com.sproutt.eussyaeussyaapi.domain.mission.MissionRepository;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@Slf4j
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
        log.info("test: {}");
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

    @AfterEach
    void after() {
        memberRepository.deleteAll();
        missionRepository.deleteAll();
        memberRepository.flush();
        missionRepository.flush();
    }

    @Test
    void 정상적인_미션조회_테스트() {
        Mission mission = missionRepository.findAll().get(0);
        ResponseEntity response = template.getForEntity("/missions/" + mission.getId(), String.class);
        log.info("Response Body: {}", response.getBody().toString());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 정상적인_미션리스트_테스트() {
        Member member = memberRepository.findAll().get(0);
        ResponseEntity<List> response = template.getForEntity("/missions?writer=" + member.getMemberId(), List.class);
        log.info("Response Body: {}", response.getBody().toString());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size() == 1);

        response = template.getForEntity("/missions?after=2020.07.14&before=2020.07.17", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size() == 1);
    }

    @Test
    void 정상적인_일시정지_테스트_러닝타임카운팅포함() {
        Mission mission = missionRepository.findAll().get(0);

        ResponseEntity response = template.exchange("/missions/" + mission.getId() + "/progress", HttpMethod.PUT, null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        response = template.getForEntity("/missions/" + mission.getId(), String.class);
        log.info("Response Body: {}", response.getBody().toString());

        template.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                           .add(tokenKey, token);
                    request.getHeaders()
                           .setZonedDateTime("date", ZonedDateTime.of(2020, 7, 15, 5, 1, 0, 0, ZoneId.of("Asia/Seoul")));
                    return execution.execute(request, body);
                }));

        response = template.exchange("/missions/" + mission.getId() + "/pause", HttpMethod.PUT, null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        response = template.getForEntity("/missions/" + mission.getId(), String.class);
        log.info("Response Body: {}", response.getBody().toString());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().toString().contains(LocalTime.of(0, 1, 0).toString()));
    }
}
