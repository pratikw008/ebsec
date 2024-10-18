package com.ezb.ebsec.exceptionHandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setHeader("ebsec-denied-reason", "Authorization failed");

        Map<String, Object> responseBody = getResponseBody(request, response, accessDeniedException);
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

    private Map<String, Object> getResponseBody(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        String currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String message
                = (accessDeniedException != null && accessDeniedException.getMessage() != null) ? accessDeniedException.getMessage() : "Unauthorized";
        String path = request.getRequestURI();

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", currentTimestamp);
        responseBody.put("status", HttpStatus.FORBIDDEN.value());
        responseBody.put("error", HttpStatus.FORBIDDEN.getReasonPhrase());
        responseBody.put("message", message);
        responseBody.put("path", path);
        return responseBody;
    }
}
