package com.sproutt.eussyaeussyaapi.api.aspect.mission;

import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.NotAvailableTimeException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;

@Component
@Aspect
public class MissionAspect {

    private static final String ZONE_SEOUL = "Asia/Seoul";
    private static final LocalTime START_AVAILABLE_LOCAL_TIME = LocalTime.of(4, 0);
    private static final LocalTime END_AVAILABLE_LOCAL_TIME = LocalTime.of(9, 0);

    @Before("@annotation(AvailableTime)")
    public void checkAvailableTime() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        LocalTime now = changeEpochMilliToLocalTime(request.getDateHeader("date"));

        if (!isAvailableTime(now)) {
            throw new NotAvailableTimeException();
        }
    }

    private LocalTime changeEpochMilliToLocalTime(long epochMilli) {
        Instant requestInstant = Instant.ofEpochMilli(epochMilli);

        return requestInstant.atZone(ZoneId.of(ZONE_SEOUL)).toLocalTime();
    }

    private boolean isAvailableTime(LocalTime now) {
        return now.isAfter(START_AVAILABLE_LOCAL_TIME) && now.isBefore(END_AVAILABLE_LOCAL_TIME);
    }
}
