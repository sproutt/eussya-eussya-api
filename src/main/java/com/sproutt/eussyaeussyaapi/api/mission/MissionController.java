package com.sproutt.eussyaeussyaapi.api.mission;

import com.sproutt.eussyaeussyaapi.api.aspect.member.LoginMember;
import com.sproutt.eussyaeussyaapi.api.aspect.mission.AvailableTime;
import com.sproutt.eussyaeussyaapi.api.member.dto.JwtMemberDTO;
import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionDTO;
import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionResponseDTO;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.application.mission.MissionService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(description = "으쌰으쌰 미션 관련 API", tags = {"Mission - 담당자 : 김종근"})
public class MissionController {

    private static final String ZONE_SEOUL = "Asia/Seoul";

    private final MemberService memberService;
    private final MissionService missionService;

    @PostMapping("/missions")
    public ResponseEntity createMission(@RequestHeader HttpHeaders requestHeaders, @LoginMember JwtMemberDTO jwtMemberDTO, @RequestBody @Valid MissionDTO missionDTO) {
        Member loginMember = memberService.findTokenOwner(jwtMemberDTO);
        missionService.create(loginMember, missionDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/missions/{missionId}")
    public ResponseEntity<MissionResponseDTO> searchMission(@PathVariable Long missionId) {
        Mission mission = missionService.findById(missionId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(new MissionResponseDTO(mission), headers, HttpStatus.OK);
    }

    @GetMapping("/missions")
    public ResponseEntity<List<MissionResponseDTO>> searchMissionList(@RequestParam(name = "writer", required = false) String memberId, @RequestParam(name = "after", required = false) String afterDate, @RequestParam(name = "before", required = false) String beforeDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Mission> missions = missionService.findAll();

        if (memberId == null) {
            return new ResponseEntity<>(missionService.changeResponseDTOList(missions), headers, HttpStatus.OK);
        }

        var writer = memberService.findByMemberId(memberId);
        missions = missionService.findByWriter(writer);

        var rangedMissions = missionService.filterDate(afterDate, beforeDate, missions);

        return new ResponseEntity<>(missionService.changeResponseDTOList(rangedMissions), headers, HttpStatus.OK);
    }

    @PutMapping("/missions/{missionId}")
    public ResponseEntity updateMission(@RequestHeader HttpHeaders requestHeaders, @LoginMember JwtMemberDTO jwtMemberDTO, @PathVariable Long missionId, @RequestBody @Valid MissionDTO missionDTO) {
        Member loginMember = memberService.findTokenOwner(jwtMemberDTO);
        missionService.update(loginMember, missionId, missionDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @DeleteMapping("/missions/{missionId}")
    public ResponseEntity deleteMission(@RequestHeader HttpHeaders requestHeaders, @LoginMember JwtMemberDTO jwtMemberDTO, @PathVariable Long missionId) {
        Member loginMember = memberService.findTokenOwner(jwtMemberDTO);
        missionService.delete(loginMember, missionId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @AvailableTime
    @PutMapping("/missions/{missionId}/pause")
    public ResponseEntity pauseMission(@RequestHeader HttpHeaders requestHeaders, @LoginMember JwtMemberDTO jwtMemberDTO, @PathVariable Long missionId) {
        Member loginMember = memberService.findTokenOwner(jwtMemberDTO);
        LocalDateTime now = changeEpochMilliToLocalTime(requestHeaders.getDate());

        missionService.pauseMission(loginMember, missionId, now);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @AvailableTime
    @PutMapping("/missions/{missionId}/progress")
    public ResponseEntity startMission(@RequestHeader HttpHeaders requestHeaders, @LoginMember JwtMemberDTO jwtMemberDTO, @PathVariable Long missionId) {
        Member loginMember = memberService.findTokenOwner(jwtMemberDTO);
        LocalDateTime now = changeEpochMilliToLocalTime(requestHeaders.getDate());

        missionService.startMission(loginMember, missionId, now);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PutMapping("/missions/{missionId}/complete")
    public ResponseEntity completeMission(@RequestHeader HttpHeaders requestHeaders, @LoginMember JwtMemberDTO jwtMemberDTO, @PathVariable Long missionId) {
        Member loginMember = memberService.findTokenOwner(jwtMemberDTO);
        LocalDateTime now = changeEpochMilliToLocalTime(requestHeaders.getDate());

        missionService.completeMission(loginMember, missionId, now);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    private LocalDateTime changeEpochMilliToLocalTime(long epochMilli) {
        Instant requestInstant = Instant.ofEpochMilli(epochMilli);

        return requestInstant.atZone(ZoneId.of(ZONE_SEOUL)).toLocalDateTime();
    }
}
