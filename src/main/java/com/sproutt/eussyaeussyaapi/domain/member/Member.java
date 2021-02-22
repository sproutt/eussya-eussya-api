package com.sproutt.eussyaeussyaapi.domain.member;

import com.sproutt.eussyaeussyaapi.api.member.dto.MemberTokenCommand;
import com.sproutt.eussyaeussyaapi.api.mission.dto.MemberDTO;
import com.sproutt.eussyaeussyaapi.domain.activity.Activity;
import com.sproutt.eussyaeussyaapi.domain.grass.Grass;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(unique = true)
    private String memberId;

    @Column
    private String password;

    @Email
    @Column(nullable = true)
    private String email;

    @Column(unique = true)
    private String nickName;

    @Column
    private String authentication;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Mission> missions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Grass> grasses = new ArrayList<>();

    @Builder
    public Member(String memberId, String password, String email, String nickName, String authentication, Provider provider, Role role) {
        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.nickName = nickName;
        this.authentication = authentication;
        this.provider = provider;
        this.role = role;
    }

    public void changeAuthCode(String authCode) {
        this.authentication = authCode;
    }

    public MemberTokenCommand toJwtInfo() {
        return MemberTokenCommand.builder()
                                 .id(this.id)
                                 .memberId(this.memberId)
                                 .nickName(this.nickName)
                                 .role(this.role)
                                 .build();
    }

    public void addMission(Mission mission) {
        missions.add(mission);
    }

    public void addActivities(Grass grass){
        grasses.add(grass);
    }

    public boolean isSame(Member member) {
        return this == member;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", nickName='" + nickName + '\'' +
                ", authentication='" + authentication + '\'' +
                ", provider=" + provider +
                ", role=" + role +
                ", missions=" + missions +
                '}';
    }

    public MemberDTO toDTO() {
        return MemberDTO.builder()
                        .id(this.id)
                        .memberId(this.memberId)
                        .nickName(this.nickName)
                        .build();
    }

    public void verifyEmail() {
        this.authentication = "Y";
    }

    public boolean isVerified() {
        return this.authentication.equals("Y");
    }
}
