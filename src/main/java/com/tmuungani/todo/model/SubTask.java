package com.tmuungani.todo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class SubTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private Task  task;
    private LocalDateTime startTime;
    private LocalDateTime dueTime;
    @Lob
    private String comment;
    private final LocalDateTime commentTime = LocalDateTime.now();
    private String nameOfCommenter;
    private boolean active;
}
