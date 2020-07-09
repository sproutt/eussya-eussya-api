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
import java.time.LocalTime;

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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_mission_writer"))
    private Member writer;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    @Column
    private LocalTime deadlineTime;

    @Column
    private LocalTime runningTime = LocalTime.of(0, 0);

    @Column
    private MissionStatus status = MissionStatus.PENDING;

    @Builder
    public Mission(String title, String contents, Member writer, LocalTime deadlineTime) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.deadlineTime = deadlineTime;
    }

    public Mission(Member writer, MissionDTO missionDTO) {
        this.title = missionDTO.getTitle();
        this.contents = missionDTO.getContents();
        this.deadlineTime = LocalTime.parse(missionDTO.getDeadlineTime());
        this.writer = writer;
    }

    public boolean isWriter(Member member) {
        return this.writer.isSame(member);
    }

    public Mission update(MissionDTO missionDTO) {
        this.title = missionDTO.getTitle();
        this.contents = missionDTO.getContents();
        this.deadlineTime = LocalTime.parse(missionDTO.getDeadlineTime());

        return this;
    }

    public boolean isDeadlinePassed(LocalTime now) {
        return deadlineTime.isBefore(now);
    }

    public void complete() {
        this.status = MissionStatus.COMPLETE;
    }

    public void passRunningTime(long runningSeconds) {
        this.runningTime = this.runningTime.plusSeconds(runningSeconds);
    }

    public void start() {
        this.status = MissionStatus.IN_PROGRESS;
    }

    public void changeStatus(MissionStatus status) {
        this.status = status;
    }
}
