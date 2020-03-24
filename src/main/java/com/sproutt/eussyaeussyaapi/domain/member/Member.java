package com.sproutt.eussyaeussyaapi.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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

    @Column(unique = true)
    private String nickName;

    @Column
    private String authentication;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;


    @Builder
    public Member(String memberId, String password, String nickName, String authentication, Provider provider) {
        this.memberId = memberId;
        this.password = password;
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
}
