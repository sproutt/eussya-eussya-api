package com.sproutt.eussyaeussyaapi.domain.activity;

import com.sproutt.eussyaeussyaapi.domain.mission.MissionCompleteEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AddActivityWithMissionCompleteEventHandler {
    private ActivityRepository activityRepository;

    public AddActivityWithMissionCompleteEventHandler(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void handle(MissionCompleteEvent event) {
        var activityType = ActivityType.ULDDO;
        activityRepository.save(new Activity(event.getMemberId(), activityType));
    }
}
