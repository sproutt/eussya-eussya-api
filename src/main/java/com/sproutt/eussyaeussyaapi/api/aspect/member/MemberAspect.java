package com.sproutt.eussyaeussyaapi.api.aspect.member;

import com.sproutt.eussyaeussyaapi.api.member.dto.MemberTokenCommand;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.NoPermissionException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@RequiredArgsConstructor
public class MemberAspect {

    private final JwtHelper jwtHelper;

    @Around("execution(* *(.., @LoginMember (*), ..))")
    public Object convertMember(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] joinPointArgs = joinPoint.getArgs();

        HttpHeaders headers = (HttpHeaders) Arrays.stream(joinPointArgs)
                                      .filter(object -> object.toString().contains("accept"))
                                      .findFirst()
                                      .orElseThrow(NoPermissionException::new);

        String token = headers.getFirst(JwtHelper.ACCESS_TOKEN_HEADER);
        MemberTokenCommand memberTokenCommand = jwtHelper.decryptToken(token);

        for (int i = 0; i < joinPointArgs.length; i++) {
            if (joinPointArgs[i].toString().contains("MemberTokenCommand")) {
                joinPointArgs[i] = memberTokenCommand;

                break;
            }
        }

        return joinPoint.proceed(joinPointArgs);
    }
}
