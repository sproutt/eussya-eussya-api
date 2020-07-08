package com.sproutt.eussyaeussyaapi.api.mission;

import com.sproutt.eussyaeussyaapi.api.member.dto.JwtMemberDTO;
import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionDTO;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.application.mission.MissionService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.NotAvailableTimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
public class MissionController {

    @Value("${jwt.header}")
    private String tokenKey;

    @Value("${jwt.secret}")
    private String secretKey;

    private static final String ZONE_SEOUL = "Asia/Seoul";
    private static final LocalTime START_AVAILABLE_LOCAL_TIME = LocalTime.of(4, 0);
    private static final LocalTime END_AVAILABLE_LOCAL_TIME = LocalTime.of(9, 0);

    private final MemberService memberService;
    private final MissionService missionService;
    private final JwtHelper jwtHelper;

    @PostMapping("/missions")
    public ResponseEntity createMission(@RequestHeader HttpHeaders requestHeaders, @RequestBody @Valid MissionDTO missionDTO) {
        String token = requestHeaders.get(tokenKey).get(0);
        Member loginMember = getTokenOwner(token);

        LocalTime now = changeEpochMilliToLocalTime(requestHeaders.getDate());

        if (!isAvailableTime(now)) {
            throw new NotAvailableTimeException();
        }

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

        if(memberId == null) {
            return new ResponseEntity<>(missionService.findAll(), headers, HttpStatus.OK);
        }

        var writer = memberService.findByMemberId(memberId);
        var missions = missionService.findByWriter(writer);

        var rangedMissions = missionService.filterDate(afterDate, beforeDate, missions);

        return new ResponseEntity<>(rangedMissions, headers, HttpStatus.OK);
    }

    @PutMapping("/missions/{missionId}")
    public ResponseEntity updateMission(@RequestHeader HttpHeaders requestHeaders, @PathVariable Long missionId, @RequestBody @Valid MissionDTO missionDTO) {
        String token = requestHeaders.get(tokenKey).get(0);
        Member loginMember = getTokenOwner(token);

        LocalTime now = changeEpochMilliToLocalTime(requestHeaders.getDate());

        if (!isAvailableTime(now)) {
            throw new NotAvailableTimeException();
        }

        missionService.update(loginMember, missionId, missionDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @DeleteMapping("/missions/{missionId}")
    public ResponseEntity deleteMission(@RequestHeader HttpHeaders requestHeaders, @PathVariable Long missionId) {
        String token = requestHeaders.get(tokenKey).get(0);
        Member loginMember = getTokenOwner(token);

        missionService.delete(loginMember, missionId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PutMapping("/missions/{missionId}/seconds")
    public ResponseEntity addProcessTime(@RequestHeader HttpHeaders requestHeaders, @PathVariable Long missionId, @RequestBody long processSeconds) {
        String token = requestHeaders.get(tokenKey).get(0);
        Member loginMember = getTokenOwner(token);

        LocalTime now = changeEpochMilliToLocalTime(requestHeaders.getDate());

        if (!isAvailableTime(now)) {
            throw new NotAvailableTimeException();
        }

        missionService.passRunningTime(loginMember, missionId, processSeconds);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PutMapping("/missions/{missionId}/progress")
    public ResponseEntity startMission(@RequestHeader HttpHeaders requestHeaders, @PathVariable Long missionId) {
        String token = requestHeaders.get(tokenKey).get(0);
        Member loginMember = getTokenOwner(token);

        LocalTime now = changeEpochMilliToLocalTime(requestHeaders.getDate());

        if (!isAvailableTime(now)) {
            throw new NotAvailableTimeException();
        }

        missionService.startMission(loginMember, missionId, now);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PutMapping("/missions/{missionId}/complete")
    public ResponseEntity completeMission(@RequestHeader HttpHeaders requestHeaders, @PathVariable Long missionId) {
        String token = requestHeaders.get(tokenKey).get(0);
        Member loginMember = getTokenOwner(token);

        LocalTime now = changeEpochMilliToLocalTime(requestHeaders.getDate());

        if (!isAvailableTime(now)) {
            throw new NotAvailableTimeException();
        }

        missionService.completeMission(loginMember, missionId, now);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    private LocalTime changeEpochMilliToLocalTime(long epochMilli) {
        Instant requestInstant = Instant.ofEpochMilli(epochMilli);

        return requestInstant.atZone(ZoneId.of(ZONE_SEOUL)).toLocalTime();
    }

    private boolean isAvailableTime(LocalTime now) {
        return now.isAfter(START_AVAILABLE_LOCAL_TIME) && now.isBefore(END_AVAILABLE_LOCAL_TIME);
    }

    private Member getTokenOwner(String token) {
        JwtMemberDTO jwtMemberDTO = jwtHelper.decryptToken(secretKey, token);
        return memberService.findTokenOwner(jwtMemberDTO);
    }
}
