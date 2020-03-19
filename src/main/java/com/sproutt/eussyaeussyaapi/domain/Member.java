package com.sproutt.eussyaeussyaapi.domain;

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

    @Email
    @Column(unique = true)
    private String memberId;

    @Column
    private String password;

    @Column(unique = true)
    private String nickName;


    @Builder
    public Member(String memberId, String password, String nickName) {
        this.memberId = memberId;
        this.password = password;
        this.nickName = nickName;
    }

    public boolean isEqualId(String memberId) {
        return this.memberId.equals(memberId);
    }

    public boolean isEqualPassword(String password) {
        return this.password.equals(password);
    }
}
