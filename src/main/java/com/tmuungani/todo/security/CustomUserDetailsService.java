package com.tmuungani.todo.security;

import com.tmuungani.todo.dao.employee.EmployeeDao;
import com.tmuungani.todo.model.employee.Employee;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public record CustomUserDetailsService(EmployeeDao employeeDao) implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> employee = employeeDao.findByUsername(username);
        if (employee.isEmpty())  throw new UsernameNotFoundException("User not found.");
        return employee.get();
    }
}
