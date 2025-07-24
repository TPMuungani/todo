package com.tmuungani.todo.dao.employee;

import com.tmuungani.todo.model.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EmployeeDao extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);
    List<Employee> findAllByActiveTrue();

    Optional<Employee> findByUsernameAndActiveTrue(String username);

    Optional<Employee> findByEmailAndActiveTrue(String email);
}
