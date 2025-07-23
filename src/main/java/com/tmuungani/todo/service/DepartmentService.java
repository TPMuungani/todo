package com.tmuungani.todo.service;

import com.tmuungani.todo.dto.ServiceResponse;

import java.util.List;

public interface DepartmentService {
    ServiceResponse<?> registerDepartment(String name);
    ServiceResponse<List<String>> getAllEmployeeDepartments();
    ServiceResponse<?> deleteDepartment(String name);
}
