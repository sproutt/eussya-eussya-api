package com.sproutt.eussyaeussyaapi.domain.mission;

import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionDTO;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_mission_writer"))
    private Member writer;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    @Column
    private int goalSeconds;

    @Column
    private int progressSeconds = 0;

    @Column
    private boolean achievement = false;

    @Builder
    public Mission(String title, String contents, Member writer, int goalSeconds) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.goalSeconds = goalSeconds;
    }

    public Mission(Member writer, MissionDTO missionDTO) {
        this.title = missionDTO.getTitle();
        this.contents = missionDTO.getContents();
        this.goalSeconds = changeHoursToSeconds(missionDTO.getGoalHours());
        this.writer = writer;
    }

    private int changeHoursToSeconds(int goalHours) {
        return goalHours * 60 * 60;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writer=" + writer +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", goalSeconds=" + goalSeconds +
                ", progressSeconds=" + progressSeconds +
                ", achievement=" + achievement +
                '}';
    }

    public boolean isSameWriter(Member member) {
        return this.writer.isSameMember(member);
    }

    public Mission update(MissionDTO missionDTO) {
        this.title = missionDTO.getTitle();
        this.contents = missionDTO.getContents();
        this.goalSeconds = changeHoursToSeconds(missionDTO.getGoalHours());

        return this;
    }

    public boolean isSatisfiedWithGoalTime() {
        return this.progressSeconds >= this.goalSeconds;
    }

    public void addProcessTime(long runningSeconds) {
        this.progressSeconds += runningSeconds;
    }

    public void complete() {
        this.achievement = true;
    }
}
