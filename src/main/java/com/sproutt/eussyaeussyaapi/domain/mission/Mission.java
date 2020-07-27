package com.sproutt.eussyaeussyaapi.domain.mission;

import com.google.gson.Gson;
import com.sproutt.eussyaeussyaapi.api.mission.dto.MissionDTO;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private LocalDateTime deadlineTime;

    @Column
    private String runningTimePoint;

    @Column
    private LocalTime runningTime;

    @Column
    private MissionStatus status = MissionStatus.PENDING;

    @Transient
    private List<RunningTimePoint> runningTimePointList = new ArrayList<>();

    @Builder
    public Mission(String title, String contents, Member writer, LocalDateTime deadlineTime) {
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.deadlineTime = deadlineTime;
    }

    public Mission(Member writer, MissionDTO missionDTO) {
        this.title = missionDTO.getTitle();
        this.contents = missionDTO.getContents();
        this.deadlineTime = LocalDateTime.ofInstant(Instant.parse(missionDTO.getDeadlineTime()), ZoneId.of("Asia/Seoul"));
        this.writer = writer;
    }

    public boolean isWriter(Member member) {
        return this.writer.isSame(member);
    }

    public Mission update(MissionDTO missionDTO) {
        this.title = missionDTO.getTitle();
        this.contents = missionDTO.getContents();
        this.deadlineTime = LocalDateTime.ofInstant(Instant.parse(missionDTO.getDeadlineTime()), ZoneId.of("Asia/Seoul"));

        return this;
    }

    public boolean isDeadlinePassed(LocalDateTime now) {
        return deadlineTime.isBefore(now);
    }

    public void recordPauseTime(LocalDateTime stopPointTime) {
        if (stopPointTime.getHour() >= 9) {
            stopPointTime = LocalDateTime.of(stopPointTime.toLocalDate(), LocalTime.of(9, 0));
        }

        Gson gson = new Gson();
        runningTimePointList = Arrays.asList(gson.fromJson(this.runningTimePoint, RunningTimePoint[].class));

        RunningTimePoint runningTimePoint = this.runningTimePointList.get(runningTimePointList.size() - 1);
        runningTimePoint.setStopPoint(stopPointTime);

        this.runningTimePoint = gson.toJson(this.runningTimePointList);
    }

    public void recordStartTime(LocalDateTime startPointTime) {
        RunningTimePoint runningTimePoint = new RunningTimePoint();
        runningTimePoint.setStartPoint(startPointTime);

        this.runningTimePointList.add(runningTimePoint);

        Gson gson = new Gson();
        this.runningTimePoint = gson.toJson(this.runningTimePointList);
    }

    public void pause() {
        this.status = MissionStatus.PENDING;
    }

    public void start() {
        this.status = MissionStatus.IN_PROGRESS;
    }

    public void complete() {
        this.status = MissionStatus.COMPLETE;
    }

    public void updateRunningTime() {
        LocalTime term = LocalTime.of(0, 0);

        Gson gson = new Gson();
        runningTimePointList = Arrays.asList(gson.fromJson(this.runningTimePoint, RunningTimePoint[].class));

        if (runningTimePointList.isEmpty()) {
            this.runningTime = term;
            return;
        }

        for (RunningTimePoint runningTimePoint : this.runningTimePointList) {
            term = term.plusSeconds(Duration.between(runningTimePoint.getStartPoint(), runningTimePoint.getStopPoint()).getSeconds());
        }

        this.runningTime = term;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writer=" + writer.getId() +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", deadlineTime=" + deadlineTime +
                ", runningTimePoint='" + runningTimePoint + '\'' +
                ", runningTime=" + runningTime +
                ", status=" + status +
                '}';
    }

    public boolean isToday(LocalDateTime stopPointTime) {
        return this.deadlineTime.toLocalDate().equals(stopPointTime.toLocalDate());
    }
}
