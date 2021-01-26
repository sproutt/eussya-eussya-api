package com.sproutt.eussyaeussyaapi.api.mission;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutt.eussyaeussyaapi.api.HeaderSetUpWithToken;
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

import static org.mockito.ArgumentMatchers.*;
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
    private Map<String, String> mockMissionRequestDTO;

    @BeforeEach
    void setUp() {
        headers = setUpHeader();
        loginMember = MemberFactory.getDefaultMember();
        missionsList = setMockMissionList();

        mockMissionRequestDTO = new HashMap<>();
        mockMissionRequestDTO.put("title", "test_title");
        mockMissionRequestDTO.put("contents", "test_contents");
        mockMissionRequestDTO.put("deadlineTime", "2020-07-15T00:00:00.000Z");
    }

    @Test
    @DisplayName("미션 등록 요청 - 정상적인 경우")
    void createMissionTest_then_return_created() throws Exception {
        headers.setZonedDateTime("date", ZonedDateTime.of(2020, 7, 4, 5, 0, 0, 0, ZoneId.of("Asia/Seoul")));

        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime(LocalDateTime.of(2020, 07, 15, 0, 0, 0))
                .build();

        Mission mission = new Mission(loginMember, missionRequestDTO);

        given(memberService.findTokenOwner(eq(loginMember.toJwtInfo()))).willReturn(loginMember);
        given(missionService.create(eq(loginMember), eq(missionRequestDTO))).willReturn(mission);

        ResultActions actions = mvc.perform(post("/missions")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockMissionRequestDTO)))
                                   .andDo(print());

        actions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("미션 등록 요청 - 입력이 잘못된 경우")
    void createMissionTest_with_invalid_input_then_return_badRequest() throws Exception {
        MissionRequestDTO wrongMissionRequestDTO = MissionRequestDTO
                .builder()
                .title("")
                .contents(" ")
                .deadlineTime(LocalDateTime.of(2020, 07, 15, 0, 0, 0))
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
    void readMissionTest_withDate_then_return_ok() throws Exception {
        given(missionService.findByWriter(eq(loginMember))).willReturn(missionsList);

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
    void updateMissionTest_then_return_ok() throws Exception {
        headers.setZonedDateTime("date", ZonedDateTime.of(2020, 7, 4, 5, 0, 0, 0, ZoneId.of("Asia/Seoul")));

        MissionRequestDTO updatedMissionRequestDTO = MissionRequestDTO
                .builder()
                .title("update")
                .contents("update_contents")
                .deadlineTime(LocalDateTime.of(2020, 07, 15, 0, 0, 0))
                .build();


        Mission updatedMission = new Mission(loginMember, updatedMissionRequestDTO);

        given(memberService.findTokenOwner(eq(loginMember.toJwtInfo()))).willReturn(loginMember);
        given(missionService.update(loginMember, 0l, updatedMissionRequestDTO)).willReturn(updatedMission);

        ResultActions actions = mvc.perform(put("/missions/0")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockMissionRequestDTO)))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("미션 수정 요청 - 입력이 잘못된 경우")
    void updateMissionTest_with_invalid_input_then_return_badRequest() throws Exception {
        headers.setZonedDateTime("date", ZonedDateTime.of(2020, 7, 4, 5, 0, 0, 0, ZoneId.of("Asia/Seoul")));

        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime(LocalDateTime.of(2020, 07, 15, 0, 0, 0))
                .build();

        MissionRequestDTO wrongUpdatedMissionRequestDTO = MissionRequestDTO
                .builder()
                .title("")
                .contents(" ")
                .deadlineTime(LocalDateTime.of(2020, 07, 15, 0, 0, 0))
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
    void deleteMissionTest_then_return_ok() throws Exception {
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
    void deleteMissionTest_when_not_matched_writer_then_return_badRequest() throws Exception {
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
    @DisplayName("미션 달성 요청 - 정상적인 경우")
    void completeMissionTest_then_return_ok() throws Exception {
        doNothing().when(missionService).completeMission(eq(loginMember), any());

        ResultActions actions = mvc.perform(put("/missions/0/complete")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("미션 달성 요청 - 목표 시간이 지나지 않았을 때 요청한 경우")
    void completeMissionTest_when_before_deadline_time_then_return_badRequest() throws Exception {
        doThrow(NotSatisfiedCondition.class).when(missionService).completeMission(any(), any());

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
    void writeMissionResultTest_when_status_complete_then_return_ok() throws Exception {
        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime(LocalDateTime.of(2020, 07, 15, 0, 0, 0))
                .build();

        Mission mission = new Mission(loginMember, missionRequestDTO);
        mission.complete();

        given(memberService.findTokenOwner(loginMember.toJwtInfo())).willReturn(loginMember);
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
    @DisplayName("미션 결과 수정 요청 - 미션이 complete 되지 않은 경우")
    void writeMissionResultTest_with_status_not_complete_then_return_badRequest() throws Exception {
        given(memberService.findTokenOwner(loginMember.toJwtInfo())).willReturn(loginMember);
        doThrow(NotCompletedMissionException.class).when(missionService).updateMissionResult(any(), anyLong(), anyString());

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
