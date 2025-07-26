package com.tmuungani.todo.dao.task;

import com.tmuungani.todo.enums.TaskStatus;
import com.tmuungani.todo.model.department.Department;
import com.tmuungani.todo.model.employee.Employee;
import com.tmuungani.todo.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TaskDao extends JpaRepository<Task, Long> {
    List<Task> findByDepartmentAndActiveTrue(Department department);
    List<Task> findByAssignedEmployeeAndActiveTrue(Employee employee);
    List<Task> findBySharedDepartmentsLikeAndActiveTrue(String sharedDepartments);
    List<Task> findByAssignedIndividualsLikeAndActiveTrue(String employee);
    List<Task> findByDueDateBeforeAndTaskStatusNot(LocalDate of, TaskStatus taskStatus);
}
