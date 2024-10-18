package com.ezb.ebsec.exceptionHandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        response.setHeader("ebsec-error-reason", "Authentication failed");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        String currentTimeStamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Map<String, Object> responseBody = getResponseBody(request, authException, currentTimeStamp);

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

    private static Map<String, Object> getResponseBody(HttpServletRequest request, AuthenticationException authException, String currentTimeStamp) {
        String message = (authException != null && authException.getMessage() != null) ? authException.getMessage() : "Unauthorized";
        String path = request.getRequestURI();

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", currentTimeStamp);
        responseBody.put("status", HttpStatus.UNAUTHORIZED.value());
        responseBody.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        responseBody.put("message", message);
        responseBody.put("path", path);
        return responseBody;
    }
}
