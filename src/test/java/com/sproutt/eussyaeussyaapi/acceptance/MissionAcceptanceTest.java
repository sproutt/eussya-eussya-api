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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private String accessTokenKey = "Authorization";

    @Value("${jwt.secret}")
    private String secretKey;

    private String token;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        missionRepository.deleteAll();
        memberRepository.flush();
        missionRepository.flush();

        Member member = MemberFactory.getDefaultMember();

        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime(LocalDateTime.of(2020,07,15, 0, 0, 0))
                .build();

        Mission mission = new Mission(member, missionRequestDTO);
        member.addMission(mission);
        log.info("test: {}");
        memberRepository.saveAndFlush(member);
        missionRepository.saveAndFlush(mission);

        token = jwtHelper.createAccessToken(MemberTokenCommand.builder()
                                                              .id(member.getId())
                                                              .memberId(member.getMemberId())
                                                              .role(member.getRole())
                                                              .nickName(member.getNickName()).build());

        template.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                           .add(accessTokenKey, token);
                    request.getHeaders()
                           .setZonedDateTime("date", ZonedDateTime.of(2020, 7, 15, 5, 0, 0, 0, ZoneId.of("Asia/Seoul")));
                    return execution.execute(request, body);
                }));
    }

    @Test
    @DisplayName("정상적인 미션조회 테스트")
    void searchMissionTest_when_request_mission_with_valid_id_then_return_missionInfo_with_ok() {
        Mission mission = missionRepository.findAll().get(0);
        ResponseEntity response = template.getForEntity("/missions/" + mission.getId(), String.class);
        log.info("Response Body: {}", response.getBody().toString());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("정상적인 미션리스트 테스트")
    void searchMissionListTest_when_request_missionList_with_valid_memberId_then_return_missionList_with_ok() {
        ResponseEntity<List> response = template.getForEntity("/missions?writer=" + MemberFactory.getDefaultMember().getMemberId(), List.class);
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
    @DisplayName("정상적인 미션완료 테스트")
    void completeMissionTest_when_request_complete_mission_then_return_ok() throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime setUpDeadlineTime = now.plusSeconds(5);

        Map<String, String> mockMissionRequestDTO = new HashMap<>();
        mockMissionRequestDTO.put("title", "test_title");
        mockMissionRequestDTO.put("contents", "test_contents");
        mockMissionRequestDTO.put("deadlineTime", setUpDeadlineTime.toString() + 'z');

        ResponseEntity response = template.postForEntity("/missions", mockMissionRequestDTO, String.class);
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
    @DisplayName("미션 결과물 수정 테스트 - 상태가 complete인 미션의 경우 정상적으로 처리됨")
    void modifyMissionResultTest_when_valid_request_then_return_ok() {
        Mission mission = missionRepository.findAll().get(0);
        mission.complete();
        missionRepository.saveAndFlush(mission);

        HttpEntity<String> httpEntity = new HttpEntity<>("test result");

        ResponseEntity response = template.exchange("/missions/" + mission.getId() + "/result", HttpMethod.PUT, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("미션_결과물_수정_테스트 - 상태가 complete가 아닌 미션의 경우 처리되지 않음")
    void modifyMissionResultTest_when_invalid_request_then_return_badRequest() {
        Mission mission = missionRepository.findAll().get(0);

        HttpEntity<String> httpEntity = new HttpEntity<>("test result");

        ResponseEntity response = template.exchange("/missions/" + mission.getId() + "/result", HttpMethod.PUT, httpEntity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
