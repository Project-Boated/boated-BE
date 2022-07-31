package com.projectboated.backend.domain.task.taskfile.repository;

import com.projectboated.backend.domain.task.taskfile.entity.TaskFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskFileRepository extends JpaRepository<TaskFile, Long> {
}
