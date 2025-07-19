package com.tmuungani.todo.dao;

import com.tmuungani.todo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmployeeDao extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);
}
