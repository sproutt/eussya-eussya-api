package com.sproutt.eussyaeussyaapi.api.aspect.mission;

import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.NotAvailableTimeException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Component
@Aspect
public class MissionAspect {

    private static final String ZONE_SEOUL = "Asia/Seoul";
    private static final int START_AVAILABLE_HOUR = 4;
    private static final int END_AVAILABLE_HOUR = 9;

    @Before("@annotation(AvailableTime)")
    public void checkAvailableTime() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        LocalDateTime now = changeEpochMilliToLocalTime(request.getDateHeader("date"));

        if (!isAvailableTime(now)) {
            throw new NotAvailableTimeException();
        }
    }

    private LocalDateTime changeEpochMilliToLocalTime(long epochMilli) {
        Instant requestInstant = Instant.ofEpochMilli(epochMilli);

        return requestInstant.atZone(ZoneId.of(ZONE_SEOUL)).toLocalDateTime();
    }

    private boolean isAvailableTime(LocalDateTime now) {
        LocalDateTime startLocalDateTime = LocalDateTime.of(now.toLocalDate(), LocalTime.of(START_AVAILABLE_HOUR, 0));
        LocalDateTime endLocalDateTime = LocalDateTime.of(now.toLocalDate(), LocalTime.of(END_AVAILABLE_HOUR, 0));

        return now.isAfter(startLocalDateTime) && now.isBefore(endLocalDateTime);
    }
}
