package com.sproutt.eussyaeussyaapi.domain.mission;

public class MissionCompleteEvent {
    private Mission mission;

    public MissionCompleteEvent(Mission mission) {
        this.mission = mission;
    }

    public String getMemberId() {
        return mission.getWriter()
                      .getMemberId();
    }
}
