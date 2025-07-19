package com.tmuungani.todo.dao;

import com.tmuungani.todo.model.Department;
import com.tmuungani.todo.model.Employee;
import com.tmuungani.todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskDao extends JpaRepository<Task, Long> {
    List<Task> findByDepartmentAndActiveTrue(Department department);
    List<Task> findByAssignedEmployeeAndActiveTrue(Employee employee);
    List<Task> findBySharedDepartmentsLikeAndActiveTrue(String sharedDepartments);
    List<Task> findByAssignedIndividualsLikeAndActiveTrue(String employee);
}
