package com.sproutt.eussyaeussyaapi.domain.file;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column
    private String nickName;

    @Column
    private String fileKey;

    @Column(unique = true)
    private String storagePath;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_mission_member"))
    private Member member;

    @Builder
    public File(Member member, String storagePath, String fileKey) {
        this.nickName = member.getNickName();
        this.member = member;
        this.storagePath = storagePath;
        this.fileKey = fileKey;
    }

    public File updateFile(String storagePath, String fileKey) {
        this.storagePath = storagePath;
        this.fileKey = fileKey;
        return this;
    }
}
