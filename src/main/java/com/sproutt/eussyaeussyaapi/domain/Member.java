package com.sproutt.eussyaeussyaapi.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    private String memberId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;


    @Builder
    public Member(String memberId, String password, String name) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
    }

    public boolean isEqualId(String memberId) {
        return this.memberId.equals(memberId);
    }

    public boolean isEqualPassword(String password) {
        return this.password.equals(password);
    }

    public Member update(Member updatedMember) {
        name = updatedMember.name;
        return this;
    }
}
