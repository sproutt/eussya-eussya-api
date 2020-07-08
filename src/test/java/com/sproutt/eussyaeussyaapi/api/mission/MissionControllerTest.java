package com.sproutt.eussyaeussyaapi.api.mission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutt.eussyaeussyaapi.api.HeaderSetUpWithToken;
import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionDTO;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.application.mission.MissionService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.NoPermissionException;
import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.NotAvailableTimeException;
import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.NotSatisfiedCondition;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MissionController.class)
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

        MissionDTO missionDTO = MissionDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime("09:00:00")
                .build();

        Mission mission = new Mission(loginMember, missionDTO);

        given(memberService.findTokenOwner(any())).willReturn(loginMember);
        given(missionService.create(any(), any())).willReturn(mission);

        ResultActions actions = mvc.perform(post("/missions")
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(missionDTO)))
                                   .andDo(print());

        actions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("미션 등록 요청 - 입력이 잘못된 경우")
    void createMissionTest_withWrongInput() throws Exception {
        MissionDTO wrongMissionDTO = MissionDTO
                .builder()
                .title("")
                .contents(" ")
                .deadlineTime("09:00:00")
                .build();

        Mission mission = new Mission(loginMember, wrongMissionDTO);

        given(missionService.create(loginMember, wrongMissionDTO)).willReturn(mission);

        ResultActions actions = mvc.perform(post("/missions")
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(wrongMissionDTO)))
                                   .andDo(print());

        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("미션 등록 요청 - 등록 가능 시간이 아닌 경우")
    void createMissionTest_withWrongTime() throws Exception {
        headers.setZonedDateTime("date", ZonedDateTime.of(2020, 7, 4, 10, 0, 0, 0, ZoneId.of("Asia/Seoul")));

        MissionDTO missionDTO = MissionDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime("09:00:00")
                .build();

        given(missionService.create(loginMember, missionDTO)).willThrow(new NotAvailableTimeException());

        ResultActions actions = mvc.perform(post("/missions")
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(missionDTO)))
                                   .andDo(print());

        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("미션 조회 요청")
    void readMissionTest_withDate() throws Exception {
        given(missionService.findByWriter(any())).willReturn(missionsList);

        ResultActions actions = mvc.perform(get("/missions")
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

        MissionDTO updatedMissionDTO = MissionDTO
                .builder()
                .title("update")
                .contents("update_contents")
                .deadlineTime("09:00")
                .build();


        Mission updatedMission = new Mission(loginMember, updatedMissionDTO);

        given(memberService.findTokenOwner(any())).willReturn(loginMember);
        given(missionService.update(loginMember, 0l, updatedMissionDTO)).willReturn(updatedMission);

        ResultActions actions = mvc.perform(put("/missions/0")
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedMissionDTO)))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("미션 수정 요청 - 입력이 잘못된 경우")
    void updateMissionTest_withWrongInput() throws Exception {
        headers.setZonedDateTime("date", ZonedDateTime.of(2020, 7, 4, 5, 0, 0, 0, ZoneId.of("Asia/Seoul")));

        MissionDTO missionDTO = MissionDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime("09:00:00")
                .build();

        MissionDTO wrongUpdatedMissionDTO = MissionDTO
                .builder()
                .title("")
                .contents(" ")
                .deadlineTime("09:00:00")
                .build();

        Mission mission = new Mission(loginMember, missionDTO);
        Mission updatedMission = new Mission(loginMember, wrongUpdatedMissionDTO);

        given(missionService.findById(0l)).willReturn(mission);
        given(missionService.update(loginMember, 0l, wrongUpdatedMissionDTO)).willReturn(updatedMission);

        ResultActions actions = mvc.perform(put("/missions/0")
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(wrongUpdatedMissionDTO)))
                                   .andDo(print());

        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("미션 수정 요청 - 수정 가능 시간이 아닌 경우")
    void updateMissionTest_withWrongTime() throws Exception {
        headers.setZonedDateTime("date", ZonedDateTime.of(2020, 7, 4, 10, 0, 0, 0, ZoneId.of("Asia/Seoul")));

        MissionDTO missionDTO = new MissionDTO()
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime("09:00:00")
                .build();

        MissionDTO updatedMissionDTO = MissionDTO
                .builder()
                .title("update")
                .contents("update_contents")
                .deadlineTime("09:00:00")
                .build();

        Mission mission = new Mission(loginMember, missionDTO);
        Mission updatedMission = new Mission(loginMember, updatedMissionDTO);

        given(missionService.findById(0l)).willReturn(mission);
        given(missionService.update(loginMember, 0l, updatedMissionDTO)).willReturn(updatedMission);

        ResultActions actions = mvc.perform(put("/missions/0")
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedMissionDTO)))
                                   .andDo(print());

        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("미션 삭제 요청 - 정상적인 경우")
    void deleteMissionTest() throws Exception {
        ResultActions actions = mvc.perform(delete("/missions/0")
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
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("미션 시작 요청")
    void startMissionTest() throws Exception {
        headers.setZonedDateTime("date", ZonedDateTime.of(2020, 7, 4, 5, 0, 0, 0, ZoneId.of("Asia/Seoul")));

        doNothing().when(missionService).startMission(any(), any(), any());

        ResultActions actions = mvc.perform(put("/missions/0/progress")
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("미션 달성 요청 - 정상적인 경우")
    void completeMissionTest() throws Exception {
        headers.setZonedDateTime("date", ZonedDateTime.of(2020, 7, 4, 5, 0, 0, 0, ZoneId.of("Asia/Seoul")));

        doNothing().when(missionService).completeMission(any(), any(), any());

        ResultActions actions = mvc.perform(put("/missions/0/complete")
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("미션 달성 요청 - 달성 가능 시간이 아닌 경우")
    void completeMissionTest_withWrongTime() throws Exception {
        headers.setZonedDateTime("date", ZonedDateTime.of(2020, 7, 4, 10, 0, 0, 0, ZoneId.of("Asia/Seoul")));

        doNothing().when(missionService).completeMission(any(), any(), any());

        ResultActions actions = mvc.perform(put("/missions/0/complete")
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("미션 달성 요청 - 목표 시간을 채우지 못한 경우")
    void completeMissionTest_withWrongProgressTime() throws Exception {
        headers.setZonedDateTime("date", ZonedDateTime.of(2020, 7, 4, 5, 0, 0, 0, ZoneId.of("Asia/Seoul")));

        doThrow(NotSatisfiedCondition.class).when(missionService).completeMission(any(), any(), any());

        ResultActions actions = mvc.perform(put("/missions/0/complete")
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
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
                                  .deadlineTime(LocalTime.of(9, 0))
                                  .writer(loginMember)
                                  .build();

        Mission mission2 = Mission.builder()
                                  .title("test2")
                                  .contents("test_contents")
                                  .deadlineTime(LocalTime.of(9, 0))
                                  .writer(loginMember)
                                  .build();

        Mission mission3 = Mission.builder()
                                  .title("test3")
                                  .contents("test_contents")
                                  .deadlineTime(LocalTime.of(9, 0))
                                  .writer(new Member())
                                  .build();

        List<Mission> mockedMissionsList = new ArrayList<>();
        mockedMissionsList.add(mission1);
        mockedMissionsList.add(mission2);
        mockedMissionsList.add(mission3);

        return mockedMissionsList;
    }
}
