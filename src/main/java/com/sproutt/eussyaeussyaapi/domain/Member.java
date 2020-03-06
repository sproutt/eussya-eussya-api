package com.sproutt.eussyaeussyaapi.domain;

import com.sproutt.eussyaeussyaapi.dto.SignUpDTO;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String memberId;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String nickName;

    @Column
    private String profileUrl;

    public Member(SignUpDTO signUpDTO) {
        this.memberId = signUpDTO.memberId;
        this.email = signUpDTO.email;
        this.name = signUpDTO.name;
        this.nickName = signUpDTO.nickName;
    }

    public void updateNickName(String newNickName) {
        this.nickName = newNickName;
    }
}
