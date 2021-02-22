package com.sproutt.eussyaeussyaapi.domain.activity;

import com.sproutt.eussyaeussyaapi.domain.grass.Grass;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Activity extends AbstractAggregateRoot<Activity> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_activity_member"))
    private Member member;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_activity_grass"))
    private Grass grass;

    @Enumerated(EnumType.STRING)
    @Column
    private ActivityStatus activityStatus;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    @Column
    private LocalDateTime deadlineTime;

    @Column
    private LocalDateTime finishTime;

    @Builder
    public Activity(ActivityStatus activityStatus, LocalDateTime createdTime, LocalDateTime deadlineTime, Member member) {
        this.activityStatus = activityStatus;
        this.createdTime = createdTime;
        this.deadlineTime = deadlineTime;
        this.member = member;
    }

    public Activity complete() {
        activityStatus = ActivityStatus.COMPLETE;
        finishTime = LocalDateTime.now();
        registerEvent(new CompleteActivityEvent(this));
        return this;
    }

}
