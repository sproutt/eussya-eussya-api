package com.sproutt.eussyaeussyaapi.application.mission;

import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionRequestDTO;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;

import java.util.List;

public interface MissionService {
    Mission create(Member loginMember, MissionRequestDTO missionRequestDTO);

    Mission update(Member loginMember, Long missionId, MissionRequestDTO missionRequestDTO);

    void delete(Member loginMember, Long missionId);

    Mission findById(Long missionId);

    List<Mission> findByWriter(Member byMemberId);

    List<Mission> findAll();

    List<Mission> filterDate(String afterDate, String beforeDate, List<Mission> missionList);

    void pauseMission(Member loginMember, Long missionId, String timeFormattedISO);

    void startMission(Member loginMember, Long missionId, String timeFormattedISO);

    void completeMission(Member loginMember, Long missionId, String timeFormattedISO);

    List<Mission> filterStatus(String status, List<Mission> rangedMissions);

    Mission addMissionResult(Member loginMember, Long missionId, String result);

}
