package com.sproutt.eussyaeussyaapi.api.mission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutt.eussyaeussyaapi.api.HeaderSetUpWithToken;
import com.sproutt.eussyaeussyaapi.api.mission.dto.CompleteMissionRequestDTO;
import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionRequestDTO;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.application.mission.MissionService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.NoPermissionException;
import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.NotCompletedMissionException;
import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.NotSatisfiedCondition;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MissionController.class, excludeFilters = @ComponentScan.Filter(EnableWebSecurity.class))
@EnableAspectJAutoProxy
@WithMockUser("fake_user")
public class MissionControllerTest extends HeaderSetUpWithToken {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MissionService missionService;

    @MockBean
    private MemberService memberService;

    private HttpHeaders headers;
    private Member loginMember;
    private List<Mission> missionsList;

    @BeforeEach
    void setUp() throws Exception {
        headers = setUpHeader();
        loginMember = MemberFactory.getDefaultMember();
        missionsList = setMockMissionList();
    }

    @Test
    @DisplayName("미션 등록 요청 - 정상적인 경우")
    void createMissionTest() throws Exception {
        headers.setZonedDateTime("date", ZonedDateTime.of(2020, 7, 4, 5, 0, 0, 0, ZoneId.of("Asia/Seoul")));

        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime("2020-07-15T00:00:00.00Z")
                .build();

        Mission mission = new Mission(loginMember, missionRequestDTO);

        given(memberService.findTokenOwner(any())).willReturn(loginMember);
        given(missionService.create(any(), any())).willReturn(mission);

        ResultActions actions = mvc.perform(post("/missions")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(missionRequestDTO)))
                                   .andDo(print());

        actions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("미션 등록 요청 - 입력이 잘못된 경우")
    void createMissionTest_withWrongInput() throws Exception {
        MissionRequestDTO wrongMissionRequestDTO = MissionRequestDTO
                .builder()
                .title("")
                .contents(" ")
                .deadlineTime("2020-07-15T00:00:00.00Z")
                .build();

        Mission mission = new Mission(loginMember, wrongMissionRequestDTO);

        given(missionService.create(loginMember, wrongMissionRequestDTO)).willReturn(mission);

        ResultActions actions = mvc.perform(post("/missions")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(wrongMissionRequestDTO)))
                                   .andDo(print());

        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("미션 조회 요청")
    void readMissionTest_withDate() throws Exception {
        given(missionService.findByWriter(any())).willReturn(missionsList);

        ResultActions actions = mvc.perform(get("/missions")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("미션 수정 요청 - 정상적인 경우")
    void updateMissionTest() throws Exception {
        headers.setZonedDateTime("date", ZonedDateTime.of(2020, 7, 4, 5, 0, 0, 0, ZoneId.of("Asia/Seoul")));

        MissionRequestDTO updatedMissionRequestDTO = MissionRequestDTO
                .builder()
                .title("update")
                .contents("update_contents")
                .deadlineTime("2020-07-15T00:00:00.00Z")
                .build();


        Mission updatedMission = new Mission(loginMember, updatedMissionRequestDTO);

        given(memberService.findTokenOwner(any())).willReturn(loginMember);
        given(missionService.update(loginMember, 0l, updatedMissionRequestDTO)).willReturn(updatedMission);

        ResultActions actions = mvc.perform(put("/missions/0")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedMissionRequestDTO)))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("미션 수정 요청 - 입력이 잘못된 경우")
    void updateMissionTest_withWrongInput() throws Exception {
        headers.setZonedDateTime("date", ZonedDateTime.of(2020, 7, 4, 5, 0, 0, 0, ZoneId.of("Asia/Seoul")));

        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime("2020-07-15T00:00:00.00Z")
                .build();

        MissionRequestDTO wrongUpdatedMissionRequestDTO = MissionRequestDTO
                .builder()
                .title("")
                .contents(" ")
                .deadlineTime("2020-07-15T00:00:00.00Z")
                .build();

        Mission mission = new Mission(loginMember, missionRequestDTO);
        Mission updatedMission = new Mission(loginMember, wrongUpdatedMissionRequestDTO);

        given(missionService.findById(0l)).willReturn(mission);
        given(missionService.update(loginMember, 0l, wrongUpdatedMissionRequestDTO)).willReturn(updatedMission);

        ResultActions actions = mvc.perform(put("/missions/0")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(wrongUpdatedMissionRequestDTO)))
                                   .andDo(print());

        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("미션 삭제 요청 - 정상적인 경우")
    void deleteMissionTest() throws Exception {
        ResultActions actions = mvc.perform(delete("/missions/0")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("미션 삭제 요청 - 작성자와 일치하지 않는 경우")
    void deleteMissionTest_WithWrongWriter() throws Exception {
        doThrow(new NoPermissionException()).when(missionService).delete(any(), any());

        ResultActions actions = mvc.perform(delete("/missions/0")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("미션 시작 요청")
    void startMissionTest() throws Exception {
        String time = "2020-07-15T05:00:00.00Z";
        Map<String, String> mapForJson = new HashMap<>();
        mapForJson.put("time", time);

        doNothing().when(missionService).completeMission(any(), any(), any());

        ResultActions actions = mvc.perform(put("/missions/0/progress")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mapForJson)))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("미션 달성 요청 - 정상적인 경우")
    void completeMissionTest() throws Exception {
        String time = "2020-07-15T05:00:00.00Z";
        CompleteMissionRequestDTO completeMissionRequestDTO = new CompleteMissionRequestDTO(time, "result contents...");

        doNothing().when(missionService).completeMission(any(), any(), any());

        ResultActions actions = mvc.perform(put("/missions/0/complete")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(completeMissionRequestDTO)))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("미션 달성 요청 - 목표 시간을 채우지 못한 경우")
    void completeMissionTest_withWrongProgressTime() throws Exception {
        headers.setZonedDateTime("date", ZonedDateTime.of(2020, 7, 4, 5, 0, 0, 0, ZoneId.of("Asia/Seoul")));

        doThrow(NotSatisfiedCondition.class).when(missionService).completeMission(any(), any(), any());

        ResultActions actions = mvc.perform(put("/missions/0/complete")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("미션 결과 수정 요청 - 정상적인 경우")
    void writeMissionResultTest() throws Exception {
        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime("2020-07-15T00:00:00.00Z")
                .build();

        Mission mission = new Mission(loginMember, missionRequestDTO);
        mission.complete();

        given(memberService.findTokenOwner(any())).willReturn(loginMember);
        given(missionService.updateMissionResult(loginMember, 0l, "test result")).willReturn(mission);

        ResultActions actions = mvc.perform(put("/missions/0/result")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString("test result")))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("미션 결과 수정 요청 - 비정상적인 경우")
    void writeMissionResultTest_with_wrong_request() throws Exception {
        given(memberService.findTokenOwner(any())).willReturn(loginMember);
        doThrow(NotCompletedMissionException.class).when(missionService).updateMissionResult(any(), any(), any());

        ResultActions actions = mvc.perform(put("/missions/0/result")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString("test result")))
                                   .andDo(print());

        actions.andExpect(status().isBadRequest());
    }

    private static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Mission> setMockMissionList() {
        Mission mission1 = Mission.builder()
                                  .title("test1")
                                  .contents("test_contents")
                                  .deadlineTime(LocalDateTime.of(2020, 7, 13, 9, 0))
                                  .writer(loginMember)
                                  .build();

        Mission mission2 = Mission.builder()
                                  .title("test2")
                                  .contents("test_contents")
                                  .deadlineTime(LocalDateTime.of(2020, 7, 13, 9, 0))
                                  .writer(loginMember)
                                  .build();

        Mission mission3 = Mission.builder()
                                  .title("test3")
                                  .contents("test_contents")
                                  .deadlineTime(LocalDateTime.of(2020, 7, 13, 9, 0))
                                  .writer(new Member())
                                  .build();

        List<Mission> mockedMissionsList = new ArrayList<>();
        mockedMissionsList.add(mission1);
        mockedMissionsList.add(mission2);
        mockedMissionsList.add(mission3);

        return mockedMissionsList;
    }
}
