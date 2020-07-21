package com.sproutt.eussyaeussyaapi.domain.mission;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RunningTimePoint implements Serializable {

    private String startPoint;
    private String stopPoint;

    public LocalDateTime getStartPoint() {
        return LocalDateTime.parse(this.startPoint);
    }

    public void setStartPoint(LocalDateTime startPoint) {
        this.startPoint = startPoint.toString();
        this.stopPoint = startPoint.toString();
    }

    public LocalDateTime getStopPoint() {
        return LocalDateTime.parse(this.stopPoint);
    }

    public void setStopPoint(LocalDateTime stopPoint) {
        this.stopPoint = stopPoint.toString();
    }

    @Override
    public String toString() {
        return "RunningTimePoint{" +
                "startPoint=" + startPoint +
                ", stopPoint=" + stopPoint +
                '}';
    }
}
