package com.projectboated.backend.domain.task.repository;

import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    long countByProject(Project project);
}
