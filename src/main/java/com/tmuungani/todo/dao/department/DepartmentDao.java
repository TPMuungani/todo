package com.tmuungani.todo.dao.department;

import com.tmuungani.todo.model.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DepartmentDao extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
    List<Department> findByActiveTrue();
}
