package com.tmuungani.todo.dao;

import com.tmuungani.todo.model.SubTask;
import com.tmuungani.todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubTaskDao extends JpaRepository<SubTask, Long> {
    List<SubTask> findByTaskAndActiveTrue(Task task);
}
