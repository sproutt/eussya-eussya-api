package com.sproutt.eussyaeussyaapi.application.mission;

import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionDTO;
import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionResponseDTO;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;

import java.time.LocalDateTime;
import java.util.List;

public interface MissionService {
    Mission create(Member loginMember, MissionDTO missionDTO);

    Mission update(Member loginMember, Long missionId, MissionDTO missionDTO);

    void delete(Member loginMember, Long missionId);

    Mission findById(Long missionId);

    List<Mission> findByWriter(Member byMemberId);

    List<Mission> findAll();

    List<Mission> filterDate(String afterDate, String beforeDate, List<Mission> missionList);

    List<MissionResponseDTO> changeResponseDTOList(List<Mission> missionList);

    void pauseMission(Member loginMember, Long missionId, LocalDateTime now);

    void startMission(Member loginMember, Long missionId, LocalDateTime now);

    void completeMission(Member loginMember, Long missionId, LocalDateTime now);
}
