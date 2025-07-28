package com.tmuungani.todo;

import com.tmuungani.todo.dao.department.DepartmentDao;
import com.tmuungani.todo.model.department.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class TodoApplication implements CommandLineRunner {
	@Autowired
	private DepartmentDao departmentDao;

	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Optional<Department> optionalDepartment = departmentDao.findByName("ADMINISTRATION");
		if (optionalDepartment.isEmpty()) {
			Department department = new Department();
			department.setName("ADMINISTRATION");
			departmentDao.save(department);
		}
	}
}
