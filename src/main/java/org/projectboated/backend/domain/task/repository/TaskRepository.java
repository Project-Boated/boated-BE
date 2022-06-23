package org.projectboated.backend.domain.task.repository;

import org.projectboated.backend.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}