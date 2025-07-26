package com.tmuungani.todo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmuungani.todo.dto.ServiceResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(401);
        ServiceResponse<?> authResponse = new ServiceResponse<>(false, "Unauthorized: Authentication required.", null);
        response.getWriter().write(new ObjectMapper().writeValueAsString(authResponse));
        response.getWriter().flush();
        response.getWriter().close();
    }
}