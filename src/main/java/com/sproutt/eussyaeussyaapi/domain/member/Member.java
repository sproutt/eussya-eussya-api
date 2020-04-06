package com.sproutt.eussyaeussyaapi.domain.member;

import com.sproutt.eussyaeussyaapi.api.member.dto.JwtMemberDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;

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


    @Builder
    public Member(String memberId, String password, String email, String nickName, String authentication, Provider provider) {
        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.nickName = nickName;
        this.authentication = authentication;
        this.provider = provider;
    }

    public boolean isEqualId(String memberId) {
        return this.memberId.equals(memberId);
    }

    public boolean isEqualPassword(String password) {
        return this.password.equals(password);
    }

    public boolean verifyEmail(String authCode) {

        if (this.authentication.equals(authCode)) {
            this.authentication = "Y";

            return true;
        }

        return false;
    }

    public void changeAuthCode(String authCode) {
        this.authentication = authCode;
    }

    public JwtMemberDTO toJwtInfo() {
        return JwtMemberDTO.builder()
                .id(this.id)
                .memberId(this.memberId)
                .email(this.email)
                .nickName(this.nickName)
                .provider(this.provider)
                .build();
    }
}
