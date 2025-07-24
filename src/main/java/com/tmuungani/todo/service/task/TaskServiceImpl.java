package com.tmuungani.todo.service.task;

import com.tmuungani.todo.dao.department.DepartmentDao;
import com.tmuungani.todo.dao.employee.EmployeeDao;
import com.tmuungani.todo.dao.subtask.SubTaskDao;
import com.tmuungani.todo.dao.task.TaskDao;
import com.tmuungani.todo.dto.ServiceResponse;
import com.tmuungani.todo.dto.SubTaskRequest;
import com.tmuungani.todo.dto.TaskRequest;
import com.tmuungani.todo.enums.TaskStatus;
import com.tmuungani.todo.exception.TodoException;
import com.tmuungani.todo.model.department.Department;
import com.tmuungani.todo.model.employee.Employee;
import com.tmuungani.todo.model.subtask.SubTask;
import com.tmuungani.todo.model.task.Task;
import com.tmuungani.todo.security.CurrentAuditor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    private final TaskDao taskDao;
    private final EmployeeDao employeeDao;
    private final SubTaskDao subTaskDao;
    private final CurrentAuditor currentAuditor;
    private final DepartmentDao departmentDao;

    @Override
    public ServiceResponse<?> createTask(TaskRequest taskRequest) {
        try {
            Task taskToSave = toTask(taskRequest);
            taskToSave.setTaskStatus(TaskStatus.ACTIVE);
            Task task = taskDao.save(toTask(taskRequest));
            if (!taskRequest.subTasks().isEmpty()) {
                taskRequest.subTasks().forEach(x -> {
                    subTaskDao.save(toSubTask(task, x));
                });
            }
            return new ServiceResponse<>(true, "Task saved successfully.", null);
        }catch (TodoException e){
            return new ServiceResponse<>(false, e.getMessage(), null);
        }
    }

    @Override
    public ServiceResponse<?> updateTask(TaskRequest taskRequest, Long id) {
        return null;
    }

    private SubTask toSubTask(Task task, SubTaskRequest x) {
        SubTask subTask = new SubTask();
        subTask.setName(x.name());
        subTask.setDescription(x.description());
        subTask.setTask(task);
        subTask.setStartTime(x.startTime());
        subTask.setDueTime(x.dueTime());
        subTask.setComment(x.comment());
        Optional<Employee> employee = currentAuditor.getCurrentAuditor();
        employee.ifPresent(x1->subTask.setNameOfCommenter(x1.getFirstName()+" "+x1.getLastName()));
        subTask.setActive(true);
        return subTask;
    }

    private Task toTask(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTaskName(taskRequest.taskName().trim());
        Optional<Employee> employee = currentAuditor.getCurrentAuditor();
        employee.ifPresent(x->
            task.setTaskCreator(x.getFirstName()+" "+x.getLastName()));
        task.setDescription(taskRequest.description());
        Optional<Department> department = departmentDao.findByName(taskRequest.department().trim().toUpperCase());
        department.ifPresent(task::setDepartment);
        if (!taskRequest.subTasks().isEmpty()) {
            AtomicReference<String> employees = new AtomicReference<>("");
            taskRequest.assignedIndividuals().forEach(x -> {
                Optional<Employee> employee1 = employeeDao.findByUsername(x.trim());
                employee1.ifPresent(x1->employees.set(employees + "," + employee1.get().getId()));
            });
            task.setAssignedIndividuals(employees.get());
        }
        Optional<Employee> assignedEmployee = employeeDao.findByUsername(taskRequest.assignedEmployee().trim());
        assignedEmployee.ifPresent(task::setAssignedEmployee);
        task.setCreatedDate(LocalDateTime.now());
        if (!taskRequest.sharedDepartments().isEmpty()) {
            AtomicReference<String> departments = new AtomicReference<>("");
            taskRequest.sharedDepartments().forEach(x -> {
                Optional<Department> department1 = departmentDao.findByName(x.trim().toUpperCase());
                department1.ifPresent(x1->departments.set(departments + "," + department1.get().getId()));
            });
            task.setSharedDepartments(departments.get());
        }
        task.setActive(true);
        return task;
    }
}
