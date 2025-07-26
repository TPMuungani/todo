package com.tmuungani.todo.service.task;

import com.tmuungani.todo.dao.department.DepartmentDao;
import com.tmuungani.todo.dao.employee.EmployeeDao;
import com.tmuungani.todo.dao.subtask.SubTaskDao;
import com.tmuungani.todo.dao.task.TaskDao;
import com.tmuungani.todo.dto.ServiceResponse;
import com.tmuungani.todo.dto.subtask.SubTaskRequest;
import com.tmuungani.todo.dto.subtask.SubTaskResponse;
import com.tmuungani.todo.dto.task.TaskRequest;
import com.tmuungani.todo.dto.task.TaskResponse;
import com.tmuungani.todo.dto.task.UpdateTaskRequest;
import com.tmuungani.todo.enums.TaskStatus;
import com.tmuungani.todo.exception.TodoException;
import com.tmuungani.todo.model.department.Department;
import com.tmuungani.todo.model.employee.Employee;
import com.tmuungani.todo.model.subtask.SubTask;
import com.tmuungani.todo.model.task.Task;
import com.tmuungani.todo.security.CurrentAuditor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
        ServiceResponse<?> checkDates = validateDates(taskRequest);
        if (checkDates.success())
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
        return checkDates;
    }

    private ServiceResponse<?> validateDates(TaskRequest taskRequest) {
        if (taskRequest.dueDate().isBefore(LocalDate.now()))
            return new ServiceResponse<>(false, "Due date cannot be before today.", null);
        List<SubTaskRequest> subTaskRequests = taskRequest.subTasks().stream().filter(x ->
                x.startTime().isAfter(x.dueTime()) || taskRequest.dueDate().isBefore(x.dueTime().toLocalDate())).toList();
        if (!subTaskRequests.isEmpty())
            return new ServiceResponse<>(false, "Subtask due time cannot be after task due date or subtask due time can not be before start time.", null);
        return new ServiceResponse<>(true, "Success", null);
    }

    private ServiceResponse<?> validateDates(UpdateTaskRequest taskRequest) {
        if (taskRequest.dueDate().isBefore(LocalDate.now()))
            return new ServiceResponse<>(false, "Due date cannot be before today.", null);
        List<SubTaskRequest> subTaskRequests = taskRequest.subTasks().stream().filter(x ->
                x.startTime().isAfter(x.dueTime()) || taskRequest.dueDate().isBefore(x.dueTime().toLocalDate())).toList();
        if (!subTaskRequests.isEmpty())
            return new ServiceResponse<>(false, "Subtask due time cannot be after task due date or subtask due time can not be before start time.", null);
        return new ServiceResponse<>(true, "Success", null);
    }

    @Override
    public ServiceResponse<?> updateTask(UpdateTaskRequest taskRequest, Long id) {
        Optional<Task> existingTask = taskDao.findById(id);
        if (existingTask.isPresent()) {
            Task task = existingTask.get();
            ServiceResponse<?> check = validateDates(taskRequest);
            if (check.success()) {
                task.setTaskName(taskRequest.taskName().trim());
                Optional<Employee> employee = currentAuditor.getCurrentAuditor();
                employee.ifPresent(x ->
                        task.setTaskCreator(x.getFirstName() + " " + x.getLastName()));
                task.setDescription(taskRequest.description());
                Optional<Department> department = departmentDao.findByName(taskRequest.department().trim().toUpperCase());
                department.ifPresent(task::setDepartment);
                if (!taskRequest.subTasks().isEmpty()) {
                    AtomicReference<String> employees = new AtomicReference<>("");
                    taskRequest.assignedIndividuals().forEach(x -> {
                        Optional<Employee> employee1 = employeeDao.findByUsername(x.trim());
                        employee1.ifPresent(x1 -> employees.set(employees + "," + employee1.get().getId()));
                    });
                    task.setAssignedIndividuals(employees.get());
                }
                Optional<Employee> assignedEmployee = employeeDao.findByUsername(taskRequest.assignedEmployee().trim());
                assignedEmployee.ifPresent(task::setAssignedEmployee);
                task.setDueDate(taskRequest.dueDate());
                if (!taskRequest.sharedDepartments().isEmpty()) {
                    AtomicReference<String> departments = new AtomicReference<>("");
                    taskRequest.sharedDepartments().forEach(x -> {
                        Optional<Department> department1 = departmentDao.findByName(x.trim().toUpperCase());
                        department1.ifPresent(x1 -> departments.set(departments + "," + department1.get().getId()));
                    });
                    task.setSharedDepartments(departments.get());
                }
                List<SubTask> subTasks = subTaskDao.findByTaskAndActiveTrue(task);
                if (!subTasks.isEmpty()) {
                    subTaskDao.deleteAll(subTasks);
                    if (!taskRequest.subTasks().isEmpty()) {
                        taskRequest.subTasks().forEach(x -> {
                            subTaskDao.save(toSubTask(task, x));
                        });
                    }
                }
                taskDao.save(task);
                return new ServiceResponse<>(true, "Task updated successfully.", null);
            }
            return check;
        }
        return new ServiceResponse<>(false, "Task does not exist.", null);
    }

    @Override
    public ServiceResponse<?> deleteTask(Long id) {
        Optional<Task> existingTask = taskDao.findById(id);
        if (existingTask.isPresent()){
            Task task = existingTask.get();
            task.setActive(false);
            taskDao.save(task);
            return new ServiceResponse<>(true, "Task deleted successfully.", null);
        }
        return new ServiceResponse<>(false, "Task does not exist.", null);
    }

    @Override
    public ServiceResponse<List<TaskResponse>> getTasksByDepartment(String departmentName) {
        Optional<Department> department = departmentDao.findByName(departmentName.trim().toUpperCase());
        if (department.isPresent()){
            List<Task> tasks = taskDao.findByDepartmentAndActiveTrue(department.get());
            List<TaskResponse> taskResponses = toTaskResponse(tasks);
            return new ServiceResponse<>(true, "Success", taskResponses);
        }
        return new ServiceResponse<>(false, "Department does not exist.", null);
    }

    @Override
    public ServiceResponse<List<TaskResponse>> getAllTasksAssignedToMe() {
        Optional<Employee> employee = currentAuditor.getCurrentAuditor();
        return getListOfTasksByEmployee(employee);
    }

    @Override
    public ServiceResponse<List<TaskResponse>> getAllTasksAssignedToACertainEmployee(String employeeUsername) {
        Optional<Employee> employee = employeeDao.findByUsername(employeeUsername.trim().toLowerCase());
        return getListOfTasksByEmployee(employee);
    }

    private ServiceResponse<List<TaskResponse>> getListOfTasksByEmployee(Optional<Employee> employee) {
        if (employee.isPresent()){
            List<Task> tasks = taskDao.findByAssignedEmployeeAndActiveTrue(employee.get());
            List<TaskResponse> taskResponses = toTaskResponse(tasks);
            return new ServiceResponse<>(true, "Success", taskResponses);
        }
        return new ServiceResponse<>(false, "Employee does not exist.", null);
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
        task.setDueDate(taskRequest.dueDate());
        return task;
    }

    private List<TaskResponse> toTaskResponse(List<Task> tasks) {
        List<TaskResponse> taskResponses = new ArrayList<>();
        tasks.forEach(x -> {
            List<SubTaskResponse> subTaskResponses = new ArrayList<>();
            List<SubTask> subTasks = subTaskDao.findByTaskAndActiveTrue(x);
            if (!subTasks.isEmpty()) {
                subTasks.forEach(v -> {
                    subTaskResponses.add(new SubTaskResponse(
                            v.getName(),v.getDescription(), v.getStartTime(), v.getDueTime(), v.getComment()
                    ));
                });
            }

            List<String> assignedEmployees = new ArrayList<>();
            List<String> assignedIndividuals = List.of(x.getAssignedIndividuals().split(","));
            assignedIndividuals.forEach(y -> {
                Optional<Employee> employee = employeeDao.findById(Long.parseLong(y));
                employee.ifPresent(z->assignedEmployees.add(z.getFirstName()+" "+z.getLastName()));
            });

            List<String> sharedDepartments = new ArrayList<>();
            List<String> sharedDepartmentsList = List.of(x.getSharedDepartments().split(","));
            sharedDepartmentsList.forEach(y -> {
                Optional<Department> department1 = departmentDao.findById(Long.parseLong(y));
                department1.ifPresent(z->sharedDepartments.add(z.getName()));
            });

            taskResponses.add(new TaskResponse(x.getId(), x.getTaskName(), x.getDescription(),x.getDepartment().getName(), assignedEmployees,x.getAssignedEmployee().getFirstName()+" "+x.getAssignedEmployee().getLastName(),
                    sharedDepartments, subTaskResponses));

        });
        return taskResponses;
    }

    @Scheduled(cron = "1 0 0 * * ?")
    public void changeTaskStatus(){
        List<Task> tasks = taskDao.findByDueDateBeforeAndTaskStatusNot(LocalDate.now(), TaskStatus.OVERDUE);
        tasks.stream().filter(x -> !x.getTaskStatus().equals(TaskStatus.ON_HOLD)).forEach(x -> {
            x.setTaskStatus(TaskStatus.OVERDUE);
            taskDao.save(x);
        });
    }
}
