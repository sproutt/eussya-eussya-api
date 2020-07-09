package com.sproutt.eussyaeussyaapi.api.mission;

import com.sproutt.eussyaeussyaapi.api.aspect.member.LoginMember;
import com.sproutt.eussyaeussyaapi.api.aspect.mission.AvailableTime;
import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionDTO;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.application.mission.MissionService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import com.sproutt.eussyaeussyaapi.domain.mission.MissionStatus;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(description = "으쌰으쌰 미션 관련 API", tags = {"Mission - 담당자 : 김종근"})
public class MissionController {

    private static final String ZONE_SEOUL = "Asia/Seoul";

    private final MemberService memberService;
    private final MissionService missionService;

    @AvailableTime
    @PostMapping("/missions")
    public ResponseEntity createMission(@LoginMember Member loginMember, @RequestBody @Valid MissionDTO missionDTO) {
        missionService.create(loginMember, missionDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/missions/{missionId}")
    public ResponseEntity<Mission> searchMission(@PathVariable Long missionId) {
        Mission mission = missionService.findById(missionId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(mission, headers, HttpStatus.OK);
    }

    @GetMapping("/missions")
    public ResponseEntity<List<Mission>> searchMissionList(@RequestParam(name = "writer", required = false) String memberId, @RequestParam(name = "after", required = false) String afterDate, @RequestParam(name = "before", required = false) String beforeDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (memberId == null) {
            return new ResponseEntity<>(missionService.findAll(), headers, HttpStatus.OK);
        }

        var writer = memberService.findByMemberId(memberId);
        var missions = missionService.findByWriter(writer);

        var rangedMissions = missionService.filterDate(afterDate, beforeDate, missions);

        return new ResponseEntity<>(rangedMissions, headers, HttpStatus.OK);
    }

    @AvailableTime
    @PutMapping("/missions/{missionId}")
    public ResponseEntity updateMission(@LoginMember Member loginMember, @PathVariable Long missionId, @RequestBody @Valid MissionDTO missionDTO) {
        missionService.update(loginMember, missionId, missionDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @DeleteMapping("/missions/{missionId}")
    public ResponseEntity deleteMission(@LoginMember Member loginMember, @PathVariable Long missionId) {
        missionService.delete(loginMember, missionId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @AvailableTime
    @PutMapping("/missions/{missionId}/seconds")
    public ResponseEntity addProcessTime(@RequestHeader HttpHeaders requestHeaders, @LoginMember Member loginMember, @PathVariable Long missionId, @RequestBody long processSeconds) {
        LocalTime now = changeEpochMilliToLocalTime(requestHeaders.getDate());

        missionService.passRunningTime(loginMember, missionId, processSeconds);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @AvailableTime
    @PutMapping("/missions/{missionId}/progress")
    public ResponseEntity startMission(@RequestHeader HttpHeaders requestHeaders, @LoginMember Member loginMember, @PathVariable Long missionId) {
        LocalTime now = changeEpochMilliToLocalTime(requestHeaders.getDate());

        missionService.changeStatus(loginMember, missionId, now, MissionStatus.IN_PROGRESS);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PutMapping("/missions/{missionId}/complete")
    public ResponseEntity completeMission(@RequestHeader HttpHeaders requestHeaders, @LoginMember Member loginMember, @PathVariable Long missionId) {
        LocalTime now = changeEpochMilliToLocalTime(requestHeaders.getDate());

        missionService.changeStatus(loginMember, missionId, now, MissionStatus.COMPLETE);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    private LocalTime changeEpochMilliToLocalTime(long epochMilli) {
        Instant requestInstant = Instant.ofEpochMilli(epochMilli);

        return requestInstant.atZone(ZoneId.of(ZONE_SEOUL)).toLocalTime();
    }
}
