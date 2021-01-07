package com.sproutt.eussyaeussyaapi.api.security.auth;

import com.google.gson.Gson;
import com.sproutt.eussyaeussyaapi.api.exception.dto.ErrorCode;
import com.sproutt.eussyaeussyaapi.api.exception.dto.ErrorResponse;
import net.minidev.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");

        Gson gson = new Gson();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.of(ErrorCode.INVALID_TOKEN);

        PrintWriter out = response.getWriter();
        out.print(gson.toJson(errorResponse));
        out.flush();
        out.close();
    }
}
