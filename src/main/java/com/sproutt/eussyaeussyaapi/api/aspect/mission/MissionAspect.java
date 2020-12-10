package com.sproutt.eussyaeussyaapi.api.aspect.mission;

import com.sproutt.eussyaeussyaapi.application.mission.ServiceTimeProvider;
import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.NotAvailableTimeException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Component
@Aspect
@RequiredArgsConstructor
public class MissionAspect {

    private static final String ZONE_SEOUL = "Asia/Seoul";
    private final ServiceTimeProvider serviceTimeProvider;

    @Before("@annotation(AvailableTime)")
    public void checkAvailableTime() {
        if (!isAvailableTime(LocalDateTime.now(ZoneId.of(ZONE_SEOUL)))) {
            throw new NotAvailableTimeException();
        }
    }

    private boolean isAvailableTime(LocalDateTime now) {
        LocalDateTime startLocalDateTime = LocalDateTime.of(now.toLocalDate(), LocalTime.of(serviceTimeProvider.getStartHour(), 0));
        LocalDateTime endLocalDateTime = LocalDateTime.of(now.toLocalDate(), LocalTime.of(serviceTimeProvider.getEndHour() - 1, 59));

        return now.isAfter(startLocalDateTime) && now.isBefore(endLocalDateTime);
    }
}
