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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MissionController {

    @Value("${jwt.header}")
    private String tokenKey;

    @Value("${jwt.secret}")
    private String secretKey;

    private static final LocalTime START_AVAILABLE_LOCAL_TIME = LocalTime.of(4, 0);
    private static final LocalTime END_AVAILABLE_LOCAL_TIME = LocalTime.of(9, 0);

    private final MemberService memberService;
    private final MissionService missionService;
    private final JwtHelper jwtHelper;

    @PostMapping("/missions")
    public ResponseEntity createMission(@RequestHeader HttpHeaders requestHeaders, @RequestBody @Valid MissionDTO missionDTO) {
        String token = requestHeaders.get(tokenKey).get(0);
        Member loginMember = getTokenOwner(token);

        if (!isAvailableTime(requestHeaders.getDate())) {
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
        List<Mission> missionList;

        if (memberId != null) {
            Member writer = memberService.findByMemberId(memberId);
            missionList = missionService.findByWriter(writer);
        } else {
            missionList = missionService.findAll();
        }

        missionList = missionService.filterDate(afterDate, beforeDate, missionList);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(missionList, headers, HttpStatus.OK);
    }

    @PutMapping("/missions/{missionId}")
    public ResponseEntity updateMission(@RequestHeader HttpHeaders requestHeaders, @PathVariable Long missionId, @RequestBody @Valid MissionDTO missionDTO) {
        String token = requestHeaders.get(tokenKey).get(0);
        Member loginMember = getTokenOwner(token);

        if (!isAvailableTime(requestHeaders.getDate())) {
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

    private boolean isAvailableTime(long epochMilli) {
        Instant requestInstant = Instant.ofEpochMilli(epochMilli);
        LocalTime now = requestInstant.atZone(ZoneId.of("Asia/Seoul")).toLocalTime();

        return now.isAfter(START_AVAILABLE_LOCAL_TIME) && now.isBefore(END_AVAILABLE_LOCAL_TIME);
    }

    private Member getTokenOwner(String token) {
        JwtMemberDTO jwtMemberDTO = jwtHelper.decryptToken(secretKey, token);
        return memberService.findTokenOwner(jwtMemberDTO);
    }
}
