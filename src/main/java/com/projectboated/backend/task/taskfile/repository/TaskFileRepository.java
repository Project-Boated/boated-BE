package com.projectboated.backend.task.taskfile.repository;

import com.projectboated.backend.task.task.entity.Task;
import com.projectboated.backend.task.taskfile.entity.TaskFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskFileRepository extends JpaRepository<TaskFile, Long> {
    List<TaskFile> findByTask(Task task);

    void deleteByTask(Task task);
}
