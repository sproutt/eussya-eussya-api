package com.sproutt.eussyaeussyaapi.domain.activity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="ACTIVITYS")
@Getter
public class Activity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="ACTIVITY_ID")
    private Long id;

    @Column(name = "MEMBER_ID")
    private String memberId;

    @Column(name = "ACTIVITY_TYPE")
    private ActivityType activityType;

    @Column(name = "CREATED_DATE")
    @CreatedDate
    private LocalDate createdDate;

    public Activity(String memberId, ActivityType activityType) {
        this.memberId = memberId;
        this.activityType = activityType;
    }

    @Builder
    public Activity(Long id, String memberId, ActivityType activityType, LocalDate createdDate) {
        this.id = id;
        this.memberId = memberId;
        this.activityType = activityType;
        this.createdDate = createdDate;
    }

    Activity() {
    }
}
