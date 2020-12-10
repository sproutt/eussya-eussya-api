package com.sproutt.eussyaeussyaapi.acceptance;

import com.sproutt.eussyaeussyaapi.api.member.dto.MemberTokenCommand;
import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionRequestDTO;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import com.sproutt.eussyaeussyaapi.domain.mission.MissionRepository;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime("2020-07-15T00:00:00.00Z")
                .build();

        Mission mission = new Mission(member, missionRequestDTO);
        member.addMission(mission);
        log.info("test: {}");
        memberRepository.saveAndFlush(member);
        missionRepository.saveAndFlush(mission);

        token = jwtHelper.createToken(secretKey, MemberTokenCommand.builder()
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
        assertThat(response.getBody().size()).isEqualTo(1);

        response = template.getForEntity("/missions?after=2020-07-14T00:00:00.00Z&before=2020-07-17T00:00:00.00Z", List.class);
        log.info("Response Body: {}", response.getBody().toString());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);

        response = template.getForEntity("/missions?status=UNCOMPLETE", List.class);
        log.info("Response Body: {}", response.getBody().toString());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(1);
    }

    @Test
    void 정상적인_미션완료_테스트() throws InterruptedException {
        missionRepository.deleteAll();
        LocalDateTime now = LocalDateTime.now();
        String setUpDeadlineTime = now.plusSeconds(5).toInstant(ZoneOffset.of("+09:00")).toString();

        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime(setUpDeadlineTime)
                .build();

        ResponseEntity response = template.postForEntity("/missions", missionRequestDTO, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Mission mission = missionRepository.findAll().get(0);

        TimeUnit.SECONDS.sleep(6);

        response = template.exchange("/missions/" + mission.getId() + "/complete", HttpMethod.PUT, null, String.class);
        log.info("\nResponse :\n {}", response.toString());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        response = template.getForEntity("/missions/" + mission.getId(), String.class);
        log.info("Response Body: {}", response.getBody().toString());
    }

    @Test
    void 정상적인_미션_결과물_수정_테스트() {
        Mission mission = missionRepository.findAll().get(0);
        mission.complete();
        missionRepository.saveAndFlush(mission);

        HttpEntity<String> httpEntity = new HttpEntity<>("test result");

        ResponseEntity response = template.exchange("/missions/" + mission.getId() + "/result", HttpMethod.PUT, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void 비정상적인_미션_결과물_수정_테스트() {
        Mission mission = missionRepository.findAll().get(0);

        HttpEntity<String> httpEntity = new HttpEntity<>("test result");

        ResponseEntity response = template.exchange("/missions/" + mission.getId() + "/result", HttpMethod.PUT, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
