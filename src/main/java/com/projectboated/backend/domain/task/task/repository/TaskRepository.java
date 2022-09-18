package com.projectboated.backend.domain.task.task.repository;

import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.project.entity.Project;
import com.projectboated.backend.domain.task.task.entity.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    long countByProject(Project project);

    @Query("select t from Task t where t.id=:taskId and t.project.id=:projectId")
    Optional<Task> findByProjectIdAndTaskId(@Param("projectId") Long projectId, @Param("taskId") Long taskId);

    List<Task> findByProject(Project project);

    @Query("select count(t) from Task t where t.kanbanLane.id=:kanbanLaneId")
    long countByKanbanLaneId(@Param("kanbanLaneId") Long kanbanLaneId);

    @Modifying
    @Query("delete from Task t where t.project=:project")
    void deleteByProject(@Param("project") Project project);

    @Query("select t from Task t where t.project.id=:projectId and t.kanbanLane.id=:laneId order by t.order")
    List<Task> findByProjectIdAndKanbanLaneId(@Param("projectId") Long projectId, @Param("laneId") Long laneId);

    @Query("select max(t.order) from Task t where t.kanbanLane=:kanbanLane")
    Integer findMaxOrder(@Param("kanbanLane") KanbanLane kanbanLane);
}
