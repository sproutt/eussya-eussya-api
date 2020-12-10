package com.sproutt.eussyaeussyaapi.application.mission;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class ServiceTimeProvider {

    @Value("${time.start}")
    private int startHour;

    @Value("${time.end}")
    private int endHour;

    @Value("${time.limit}")
    private int limitHour;

    @Override
    public String toString() {
        return "ServiceTimeInfo{" +
                "startHour=" + startHour +
                ", endHour=" + endHour +
                ", limitHour=" + limitHour +
                '}';
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getLimitHour() {
        return limitHour;
    }

    public LocalDateTime now() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
