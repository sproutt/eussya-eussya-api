package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionDTO;
import com.sproutt.eussyaeussyaapi.application.mission.MissionService;
import com.sproutt.eussyaeussyaapi.application.mission.MissionServiceImpl;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import com.sproutt.eussyaeussyaapi.domain.mission.MissionRepository;
import com.sproutt.eussyaeussyaapi.domain.mission.MissionStatus;
import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.NoSuchMissionException;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MissionServiceTest {
    private MissionRepository missionRepository = mock(MissionRepository.class);
    private MissionService missionService;
    private Member loginMember;
    private List<Mission> mockedMissionList;

    @BeforeEach
    void setUp() {
        missionService = new MissionServiceImpl(missionRepository);
        loginMember = MemberFactory.getDefaultMember();
        mockedMissionList = setMockMissionList();
    }

    @Test
    @DisplayName("미션 생성 테스트")
    void createMission() {
        MissionDTO missionDTO = MissionDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime("2020-07-15T00:00:00.00Z")
                .build();

        Mission mission = new Mission(loginMember, missionDTO);

        when(missionRepository.save(any(Mission.class))).thenReturn(mission);
        Mission savedMission = missionService.create(loginMember, missionDTO);

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
        MissionDTO missionDTO = MissionDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime("2020-07-15T00:00:00.00Z")
                .build();

        Mission mission = new Mission(loginMember, missionDTO);

        MissionDTO updatedMissionDTO = MissionDTO
                .builder()
                .title("test_title2")
                .contents("test_contents2")
                .deadlineTime("2020-07-15T00:00:00.00Z")
                .build();

        when(missionRepository.findById(0l)).thenReturn(Optional.of(mission));
        when(missionRepository.save(any())).thenReturn(new Mission(loginMember, updatedMissionDTO));

        Mission updatedMission = missionService.update(loginMember, 0l, missionDTO);

        assertEquals(updatedMissionDTO.getTitle(), updatedMission.getTitle());
    }

    @Test
    @DisplayName("미션 삭제 테스트")
    void deleteMission() {
        MissionDTO missionDTO = MissionDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime("2020-07-15T00:00:00.00Z")
                .build();

        Mission mission = new Mission(loginMember, missionDTO);

        when(missionRepository.findById(0l)).thenReturn(Optional.of(mission));
        missionService.delete(loginMember, 0l);
        when(missionRepository.findById(0l)).thenReturn(Optional.empty());

        assertThrows(NoSuchMissionException.class, () -> missionService.findById(0l));
    }

    @Test
    @DisplayName("미션 시작 테스트")
    void startMission() {
        MissionDTO missionDTO = MissionDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime("2020-07-15T00:00:00.00Z")
                .build();

        Mission mission = new Mission(loginMember, missionDTO);

        when(missionRepository.findById(any())).thenReturn(Optional.of(mission));
        missionService.startMission(loginMember, 0l, LocalDateTime.of(2020, 7, 13, 5, 1));

        assertEquals(MissionStatus.IN_PROGRESS, missionService.findById(0l).getStatus());
    }

    @Test
    @DisplayName("미션 완료 테스트")
    void completeMission() {
        MissionDTO missionDTO = MissionDTO
                .builder()
                .title("test_title")
                .contents("test_contents")
                .deadlineTime("2020-07-15T00:00:00.00Z")
                .build();

        Mission mission = new Mission(loginMember, missionDTO);

        when(missionRepository.findById(any())).thenReturn(Optional.of(mission));
        missionService.startMission(loginMember, 0l, LocalDateTime.of(2020, 7, 13, 5, 1));
        missionService.completeMission(loginMember, 0l, LocalDateTime.of(2020, 7, 15, 9, 1));

        assertEquals(MissionStatus.COMPLETE, missionService.findById(0l).getStatus());
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
