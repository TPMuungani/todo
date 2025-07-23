package com.tmuungani.todo.service;

import com.tmuungani.todo.dao.DepartmentDao;
import com.tmuungani.todo.dto.ServiceResponse;
import com.tmuungani.todo.model.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{
    private final DepartmentDao departmentDao;
    @Override
    public ServiceResponse<?> registerDepartment(String name) {
        if (departmentDao.findByName(name.trim().toUpperCase()).isPresent())
            return new ServiceResponse<>(false, "Department already exist.", null);
        try {
            Department department = new Department();
            department.setName(name.trim().toUpperCase());
            department.setActive(true);
            departmentDao.save(department);
            return new ServiceResponse<>(true, "Department created successfully.", null);
        }catch (Exception e){
            return new ServiceResponse<>(false, "Failed to create department " + e, null);
        }
    }

    @Override
    public ServiceResponse<List<String>> getAllEmployeeDepartments() {
        List<Department> departments = departmentDao.findByActiveTrue();
        List<String> departmentNames = departments.stream().map(Department::getName).toList();
        return new ServiceResponse<>(true, "Success", departmentNames);
    }

    @Override
    public ServiceResponse<?> deleteDepartment(String name) {
        Department department = departmentDao.findByName(name.trim().toUpperCase()).orElse(null);
        if (department != null){
            department.setActive(false);
            departmentDao.save(department);
            return new ServiceResponse<>(true, "Department deleted successfully.", null);
        }
        return new ServiceResponse<>(false, "Department does not exist.", null);
    }


}
