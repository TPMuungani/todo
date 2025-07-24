package com.tmuungani.todo.security;

import com.tmuungani.todo.model.employee.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Slf4j
@Component
public class CurrentAuditor implements AuditorAware<Employee> {

    @Override
    public Optional<Employee> getCurrentAuditor() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                if (authentication.getPrincipal() instanceof Employee employee) {
                    return Optional.of(employee);
                }
            }
        } catch (Exception e) {
            System.out.printf("Authentication:%s%n", e.getMessage());
        }
        return Optional.empty();
    }


    public Employee getLoggedInUserOrThrow() throws Exception {
        return getCurrentAuditor().orElseThrow(() ->  new Exception("Invalid login user"));
    }

    public String getUsernameOrThrow() throws Exception {
        return getLoggedInUserOrThrow().getUsername();
    }
}