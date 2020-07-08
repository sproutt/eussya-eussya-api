package com.sproutt.eussyaeussyaapi.application.mission;

import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionDTO;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import com.sproutt.eussyaeussyaapi.domain.mission.MissionRepository;
import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.NoPermissionException;
import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.NoSuchMissionException;
import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.NotSatisfiedCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;

    @Override
    public Mission create(Member loginMember, MissionDTO missionDTO) {
        Mission mission = new Mission(loginMember, missionDTO);

        return missionRepository.save(mission);
    }

    @Override
    public Mission update(Member loginMember, Long missionId, MissionDTO missionDTO) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(NoSuchMissionException::new);

        if (!mission.isWriter(loginMember)) {
            throw new RuntimeException();
        }

        return missionRepository.save(mission.update(missionDTO));
    }

    @Override
    public void delete(Member loginMember, Long missionId) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(NoSuchMissionException::new);

        if (!mission.isWriter(loginMember)) {
            throw new NoPermissionException();
        }

        missionRepository.delete(mission);
    }

    @Override
    public Mission findById(Long missionId) {
        return missionRepository.findById(missionId).orElseThrow(NoSuchMissionException::new);
    }

    @Override
    public List<Mission> findByWriter(Member writer) {
        return missionRepository.findAllByWriter(writer);
    }

    @Override
    public List<Mission> findAll() {
        return missionRepository.findAll();
    }

    @Override
    public List<Mission> filterDate(String afterDate, String beforeDate, List<Mission> missionList) {
        if (afterDate != null) {
            LocalDateTime afterLocalDateTime = LocalDateTime.parse(afterDate);
            missionList = missionList.stream()
                                     .filter(mission -> mission.getCreatedTime().isAfter(afterLocalDateTime))
                                     .collect(Collectors.toList());
        }

        if (beforeDate != null) {
            LocalDateTime beforeLocalDateTime = LocalDateTime.parse(beforeDate);
            missionList = missionList.stream()
                                     .filter(mission -> mission.getCreatedTime().isBefore(beforeLocalDateTime))
                                     .collect(Collectors.toList());
        }

        return missionList;
    }

    @Override
    public void completeMission(Member loginMember, Long missionId, LocalTime now) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(NoSuchMissionException::new);

        if (!mission.isWriter(loginMember)) {
            throw new NoPermissionException();
        }

        if (!mission.isDeadlinePassed(now)) {
            throw new NotSatisfiedCondition();
        }

        mission.complete();
        missionRepository.save(mission);
    }

    @Override
    public void passRunningTime(Member loginMember, Long missionId, long runningSeconds) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(NoSuchMissionException::new);

        if (!mission.isWriter(loginMember)) {
            throw new NoPermissionException();
        }

        mission.passRunningTime(runningSeconds);
        missionRepository.save(mission);
    }

    @Override
    public void startMission(Member loginMember, Long missionId, LocalTime now) {
        Mission mission = missionRepository.findById(missionId).orElseThrow(NoSuchMissionException::new);

        if (!mission.isWriter(loginMember)) {
            throw new NoPermissionException();
        }

        if (!mission.isDeadlinePassed(now)) {
            throw new NotSatisfiedCondition();
        }

        mission.start();
        missionRepository.save(mission);
    }
}
