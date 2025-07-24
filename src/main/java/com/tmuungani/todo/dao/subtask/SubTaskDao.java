package com.tmuungani.todo.dao.subtask;

import com.tmuungani.todo.model.subtask.SubTask;
import com.tmuungani.todo.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubTaskDao extends JpaRepository<SubTask, Long> {
    List<SubTask> findByTaskAndActiveTrue(Task task);
}
