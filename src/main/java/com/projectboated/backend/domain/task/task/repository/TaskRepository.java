package com.projectboated.backend.domain.task.task.repository;

import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    long countByProject(Project project);

    @Query("select t from Task t where t.id=:taskId and t.project.id=:projectId")
    Optional<Task> findByProjectIdAndTaskId(@Param("projectId") Long projectId, @Param("taskId") Long taskId);
}
