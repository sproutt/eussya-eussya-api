package com.sproutt.eussyaeussyaapi.domain.mission;

import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionRequestDTO;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.*;

@Entity
@Getter
@NoArgsConstructor
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 80)
    private String title;

    @Lob
    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_mission_writer"))
    private Member writer;

    @Lob
    @Column
    private String result;

    @Column
    private LocalDateTime startTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    @Column
    @NotNull
    private LocalDateTime deadlineTime;

    @Column
    private LocalTime runningTime;

    @Column
    private MissionStatus status = MissionStatus.IN_PROGRESS;

    @Builder
    public Mission(String title, String contents, Member writer, LocalDateTime deadlineTime) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.deadlineTime = deadlineTime;
    }

    public Mission(Member writer, MissionRequestDTO missionRequestDTO) {
        this.title = missionRequestDTO.getTitle();
        this.contents = missionRequestDTO.getContents();
        this.deadlineTime = missionRequestDTO.getDeadlineTime();
        this.writer = writer;
    }

    public boolean isWriter(Member member) {
        return this.writer.isSame(member);
    }

    public Mission update(MissionRequestDTO missionRequestDTO) {
        this.title = missionRequestDTO.getTitle();
        this.contents = missionRequestDTO.getContents();
        this.deadlineTime = missionRequestDTO.getDeadlineTime();

        return this;
    }

    public boolean isDeadlinePassed(LocalDateTime now) {
        return deadlineTime.isBefore(now);
    }

    public void complete() {
        this.status = MissionStatus.COMPLETE;
    }

    public boolean isComplete() {
        return this.status == MissionStatus.COMPLETE;
    }

    public Mission addResult(String result) {
        this.result = result;
        return this;
    }

    public void recordRunningTime() {
        Duration duration = Duration.between(this.startTime, this.deadlineTime);
        this.runningTime = LocalTime.ofSecondOfDay(duration.getSeconds());
    }

    public void startAt(LocalDateTime now) {
        this.startTime = now;
    }

    public void fail() {
        this.status = MissionStatus.FAIL;
        this.runningTime = LocalTime.ofSecondOfDay(0l);
    }
}
