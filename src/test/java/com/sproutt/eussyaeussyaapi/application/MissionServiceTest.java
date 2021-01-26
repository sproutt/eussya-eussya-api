package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionRequestDTO;
import com.sproutt.eussyaeussyaapi.application.mission.MissionService;
import com.sproutt.eussyaeussyaapi.application.mission.MissionServiceImpl;
import com.sproutt.eussyaeussyaapi.application.mission.ServiceTimeProperties;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import com.sproutt.eussyaeussyaapi.domain.mission.MissionRepository;
import com.sproutt.eussyaeussyaapi.domain.mission.MissionStatus;
import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.*;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MissionServiceTest {
    private MissionRepository missionRepository = mock(MissionRepository.class);
    private ServiceTimeProperties serviceTimeProperties = mock(ServiceTimeProperties.class);
    private MissionService missionService;
    private Member loginMember;
    private List<Mission> mockedMissionList;
    private LocalDateTime mockDeadlineTime;
    private LocalDateTime mockNow;

    @BeforeEach
    void setUp() {
        missionService = new MissionServiceImpl(missionRepository, serviceTimeProperties);
        loginMember = MemberFactory.getDefaultMember();
        mockedMissionList = setMockMissionList();

        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        mockDeadlineTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 8, 0, 0);
        mockNow = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 8, 30, 0);
    }

    @Test
    @DisplayName("미션 생성 테스트")
    void createMission() {
        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime(mockDeadlineTime)
                .build();

        Mission mission = new Mission(loginMember, missionRequestDTO);

        when(missionRepository.save(any(Mission.class))).thenReturn(mission);
        Mission savedMission = missionService.create(loginMember, missionRequestDTO);

        assertEquals(mission.getTitle(), savedMission.getTitle());
    }

    @Test
    @DisplayName("미션 조회 테스트")
    void searchMission() {
        Mission mission = Mission.builder()
                                 .title("test")
                                 .contents("test_contents")
                                 .deadlineTime(LocalDateTime.of(2020, 7, 13, 9, 0))
                                 .writer(loginMember)
                                 .build();

        when(missionRepository.findById(any())).thenReturn(Optional.of(mission));

        Mission searchedMission = missionService.findById(mission.getId());

        assertEquals(mission.getTitle(), searchedMission.getTitle());
    }

    @Test
    @DisplayName("미션리스트 작성자 기준 조회 테스트")
    void searchMissionList_withWriter() {
        List<Mission> filteredMissionList = mockedMissionList.stream()
                                                             .filter(mission -> mission.getWriter().equals(loginMember))
                                                             .collect(Collectors.toList());

        when(missionRepository.findAllByWriter(loginMember))
                .thenReturn(filteredMissionList);

        List<Mission> missionList = missionService.findByWriter(loginMember);

        assertEquals(filteredMissionList.size(), missionList.size());
    }

    @Test
    @DisplayName("미션리스트 날짜 기준 조회 테스트")
    void searchMissionList_withDate() {
        List<Mission> missionList = missionService.filterDate(null, null, mockedMissionList);

        assertEquals(3, missionList.size());
    }

    @Test
    @DisplayName("미션 수정 테스트")
    void updateMission() {
        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime(mockDeadlineTime)
                .build();

        Mission mission = new Mission(loginMember, missionRequestDTO);

        MissionRequestDTO updatedMissionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title2")
                .contents("test_contents2")
                .deadlineTime(mockDeadlineTime)
                .build();

        when(missionRepository.findById(0l)).thenReturn(Optional.of(mission));
        when(missionRepository.save(any())).thenReturn(new Mission(loginMember, updatedMissionRequestDTO));

        Mission updatedMission = missionService.update(loginMember, 0l, missionRequestDTO);

        assertEquals(updatedMissionRequestDTO.getTitle(), updatedMission.getTitle());
    }

    @Test
    @DisplayName("미션 수정 테스트 - 이미 완료된 테스트인 경우 수정 금지")
    void updateMission_when_already_completed_then_throw_exception() {
        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime(mockDeadlineTime)
                .build();

        Mission mission = new Mission(loginMember, missionRequestDTO);
        mission.complete();

        MissionRequestDTO updatedMissionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title2")
                .contents("test_contents2")
                .deadlineTime(mockDeadlineTime)
                .build();

        when(missionRepository.findById(0l)).thenReturn(Optional.of(mission));
        when(missionRepository.save(any())).thenReturn(new Mission(loginMember, updatedMissionRequestDTO));

        assertThrows(CompletedMissionException.class, () -> missionService.update(loginMember, 0l, missionRequestDTO));
    }

    @Test
    @DisplayName("미션 삭제 테스트")
    void deleteMission() {
        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime(mockDeadlineTime)
                .build();

        Mission mission = new Mission(loginMember, missionRequestDTO);

        when(missionRepository.findById(0l)).thenReturn(Optional.of(mission));
        missionService.delete(loginMember, 0l);
        when(missionRepository.findById(0l)).thenReturn(Optional.empty());

        assertThrows(NoSuchMissionException.class, () -> missionService.findById(0l));
    }

    @Test
    @DisplayName("미션 완료 테스트")
    void completeMission() {
        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime(mockDeadlineTime)
                .build();

        Mission mission = new Mission(loginMember, missionRequestDTO);
        mission.startAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        when(missionRepository.findById(0l)).thenReturn(Optional.of(mission));

        mission.startAt(LocalDateTime.of(mockNow.getYear(), mockNow.getMonth(), mockNow.getDayOfMonth(), 7, 0, 0));

        when(missionRepository.findById(0l)).thenReturn(Optional.of(mission));
        when(serviceTimeProperties.now()).thenReturn(mockNow);
        when(serviceTimeProperties.getLimitHour()).thenReturn(12);

        missionService.completeMission(loginMember, 0l);

        assertEquals(MissionStatus.COMPLETE, missionService.findById(0l).getStatus());
    }

    @Test
    @DisplayName("미션 완료 테스트 - 미션 완료 제한 시간을 초과한 경우 실패")
    void completeMission_when_time_over_then_throw_exception() {
        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime(mockDeadlineTime)
                .build();

        Mission mission = new Mission(loginMember, missionRequestDTO);
        mission.startAt(LocalDateTime.of(mockNow.getYear(), mockNow.getMonth(), mockNow.getDayOfMonth(), 7, 0, 0));

        when(missionRepository.findById(0l)).thenReturn(Optional.of(mission));
        when(serviceTimeProperties.now()).thenReturn(mockNow.plusHours(5));
        when(serviceTimeProperties.getLimitHour()).thenReturn(12);

        assertThrows(LimitedTimeExceedException.class, () -> missionService.completeMission(loginMember, 0l));
    }

    @Test
    @DisplayName("미션 완료 테스트 - 목표 시간을 채우지 못한 경우 실패")
    void completeMission_when_before_deadline_time_then_throw_exception() {
        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime(mockDeadlineTime)
                .build();

        Mission mission = new Mission(loginMember, missionRequestDTO);
        mission.startAt(LocalDateTime.of(mockNow.getYear(), mockNow.getMonth(), mockNow.getDayOfMonth(), 7, 0, 0));

        when(missionRepository.findById(0l)).thenReturn(Optional.of(mission));
        when(serviceTimeProperties.now()).thenReturn(mockNow.minusHours(1));
        when(serviceTimeProperties.getLimitHour()).thenReturn(12);

        assertThrows(NotSatisfiedCondition.class, () -> missionService.completeMission(loginMember, 0l));
    }

    @Test
    @DisplayName("미션 완료 테스트 - 이미 완료된 미션인 경우 실패")
    void completeMission_when_already_completed_then_throw_exception() {
        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime(mockDeadlineTime)
                .build();

        Mission mission = new Mission(loginMember, missionRequestDTO);
        mission.startAt(LocalDateTime.of(mockNow.getYear(), mockNow.getMonth(), mockNow.getDayOfMonth(), 7, 0, 0));

        when(missionRepository.findById(0l)).thenReturn(Optional.of(mission));
        when(serviceTimeProperties.now()).thenReturn(mockNow.plusHours(5));
        when(serviceTimeProperties.getLimitHour()).thenReturn(12);

        mission.complete();

        assertThrows(ExpiredMissionException.class, () -> missionService.completeMission(loginMember, 0l));
    }

    @Test
    @DisplayName("미션 결과 추가 테스트 - 미션의 상태가 complete 인 경우에만 성공")
    void addMissionResult_when_status_complete_then_success() {
        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime(mockDeadlineTime)
                .build();

        Mission mission = new Mission(loginMember, missionRequestDTO);
        mission.complete();

        when(missionRepository.findById(any())).thenReturn(Optional.of(mission));
        when(missionRepository.save(any())).thenReturn(mission);

        assertEquals(mission, missionService.updateMissionResult(loginMember, 0l, "test"));
    }

    @Test
    @DisplayName("미션 결과 추가 테스트 - 미션 상태가 complete가 아닌 경우 실패")
    void addMissionResult_with_status_not_complete_then_throw_exception() {
        MissionRequestDTO missionRequestDTO = MissionRequestDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime(mockDeadlineTime)
                .build();

        Mission mission = new Mission(loginMember, missionRequestDTO);

        when(missionRepository.findById(any())).thenReturn(Optional.of(mission));
        when(missionRepository.save(any())).thenReturn(mission);

        assertThrows(NotCompletedMissionException.class, () -> missionService.updateMissionResult(loginMember, 0l, "test"));
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
