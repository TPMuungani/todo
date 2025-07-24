package com.tmuungani.todo.model.task;

import com.tmuungani.todo.enums.TaskStatus;
import com.tmuungani.todo.model.department.Department;
import com.tmuungani.todo.model.employee.Employee;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskName;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;
    private String assignedIndividuals; // comma seperated ids in form of a string
    @ManyToOne(fetch = FetchType.LAZY)
    private Employee assignedEmployee;
    private String taskCreator;
    private LocalDateTime createdDate;
    private String sharedDepartments; // comma seperated ids in form of a string
    private boolean active;
    private TaskStatus taskStatus;
}
