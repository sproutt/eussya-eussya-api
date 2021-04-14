package com.sproutt.eussyaeussyaapi.domain.member;

import com.sproutt.eussyaeussyaapi.api.member.dto.MemberTokenCommand;
import com.sproutt.eussyaeussyaapi.api.mission.dto.MemberDTO;
import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor
public class Member {
    @Value("${cloud.aws.s3.profile.default}")
    public String defaultProfilePath;

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

    @Column(nullable = false, columnDefinition = "varchar(255) default 'https://dugjnp7kky4tj.cloudfront.net/default_profile.jpg'")
    private String profilePath;

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

    public boolean isSame(Member member) {
        return this == member;
    }

    @Override
    public String toString() {
        return "Member{" +
                "defaultProfilePath='" + defaultProfilePath + '\'' +
                ", id=" + id +
                ", memberId='" + memberId + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", nickName='" + nickName + '\'' +
                ", authentication='" + authentication + '\'' +
                ", provider=" + provider +
                ", role=" + role +
                ", missions=" + missions +
                ", profilePath='" + profilePath + '\'' +
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

    public void saveProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
