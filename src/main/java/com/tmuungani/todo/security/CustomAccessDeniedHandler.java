package com.tmuungani.todo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmuungani.todo.dto.ServiceResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(403);
        ServiceResponse<?> authResponse = new ServiceResponse<>(false, "Unauthorized: Insufficient privileges.",null);
        response.getWriter().write(new ObjectMapper().writeValueAsString(authResponse));
        response.getWriter().flush();
        response.getWriter().close();
    }
}