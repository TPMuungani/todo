package com.tmuungani.todo.dao;

import com.tmuungani.todo.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DepartmentDao extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
}
