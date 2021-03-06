package com.sproutt.eussyaeussyaapi.api.mission;

import com.sproutt.eussyaeussyaapi.api.aspect.member.LoginMember;
import com.sproutt.eussyaeussyaapi.api.aspect.mission.AvailableTime;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberTokenCommand;
import com.sproutt.eussyaeussyaapi.api.mission.dto.CompletedMissionResponseDTO;
import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionRequestDTO;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(description = "으쌰으쌰 미션 관련 API", tags = {"Mission - 담당자 : 김종근"})
public class MissionController {

    private final MemberService memberService;
    private final MissionService missionService;

    @AvailableTime
    @PostMapping("/missions")
    public ResponseEntity createMission(@RequestHeader HttpHeaders requestHeaders,
                                        @LoginMember MemberTokenCommand memberTokenCommand,
                                        @RequestBody @Valid MissionRequestDTO missionRequestDTO) {

        Member loginMember = memberService.findTokenOwner(memberTokenCommand);
        missionService.create(loginMember, missionRequestDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/missions/{missionId}")
    public ResponseEntity<MissionResponseDTO> searchMission(@PathVariable Long missionId) {
        Mission mission = missionService.findById(missionId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MissionResponseDTO missionResponseDTO = determineMissionResponseDTO(mission);

        return new ResponseEntity<>(missionResponseDTO, headers, HttpStatus.OK);
    }

    private MissionResponseDTO determineMissionResponseDTO(Mission mission) {
        if (mission.isComplete()) {
            return new CompletedMissionResponseDTO(mission);
        }

        return new MissionResponseDTO(mission);
    }

    @GetMapping("/missions")
    public ResponseEntity<List<MissionResponseDTO>> searchMissionList(@RequestParam(name = "writer", required = false) String memberId,
                                                                      @RequestParam(name = "after", required = false) String afterDate,
                                                                      @RequestParam(name = "before", required = false) String beforeDate,
                                                                      @RequestParam(name = "status", required = false) String status) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Mission> missions = missionService.findAll();

        if (memberId != null) {
            var writer = memberService.findByMemberId(memberId);
            missions = missionService.findByWriter(writer);
        }

        var rangedMissions = missionService.filterDate(afterDate, beforeDate, missions);
        rangedMissions = missionService.filterStatus(status, rangedMissions);

        return new ResponseEntity<>(changeResponseDTOList(rangedMissions), headers, HttpStatus.OK);
    }

    public List<MissionResponseDTO> changeResponseDTOList(List<Mission> missionList) {
        List<MissionResponseDTO> responseList = new ArrayList<>();
        missionList.forEach(mission -> responseList.add(determineMissionResponseDTO(mission)));

        return responseList;
    }

    @PutMapping("/missions/{missionId}")
    public ResponseEntity updateMission(@RequestHeader HttpHeaders requestHeaders,
                                        @LoginMember MemberTokenCommand memberTokenCommand,
                                        @PathVariable Long missionId,
                                        @RequestBody @Valid MissionRequestDTO missionRequestDTO) {

        Member loginMember = memberService.findTokenOwner(memberTokenCommand);
        missionService.update(loginMember, missionId, missionRequestDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @DeleteMapping("/missions/{missionId}")
    public ResponseEntity deleteMission(@RequestHeader HttpHeaders requestHeaders,
                                        @LoginMember MemberTokenCommand memberTokenCommand,
                                        @PathVariable Long missionId) {

        Member loginMember = memberService.findTokenOwner(memberTokenCommand);
        missionService.delete(loginMember, missionId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PutMapping("/missions/{missionId}/complete")
    public ResponseEntity completeMission(@RequestHeader HttpHeaders requestHeaders,
                                          @LoginMember MemberTokenCommand memberTokenCommand,
                                          @PathVariable Long missionId) {

        Member loginMember = memberService.findTokenOwner(memberTokenCommand);

        missionService.completeMission(loginMember, missionId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PutMapping("/missions/{missionId}/result")
    public ResponseEntity updateMissionResult(@RequestHeader HttpHeaders requestHeaders,
                                              @LoginMember MemberTokenCommand memberTokenCommand,
                                              @PathVariable Long missionId,
                                              @RequestBody String result) {

        Member loginMember = memberService.findTokenOwner(memberTokenCommand);

        missionService.updateMissionResult(loginMember, missionId, result);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
