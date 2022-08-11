package com.projectboated.backend.domain.kanban.kanbanlane.repository;

import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;

import com.projectboated.backend.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface KanbanLaneRepository extends JpaRepository<KanbanLane, Long> {

    Optional<KanbanLane> findByKanbanAndName(Kanban kanban, String name);

    @Query("select count(kl) from KanbanLane kl where kl.kanban=:kanban")
    Long countByKanban(@Param("kanban") Kanban kanban);

    @Modifying
    @Query("delete from KanbanLane kl where kl.kanban=:kanban")
    int deleteByKanban(@Param("kanban") Kanban kanban);
	
    @Query("select kl from KanbanLane kl inner join Kanban k on kl.id=:kanbanLaneId and kl.kanban.id=k.id and k.project.id=:projectId")
	Optional<KanbanLane> findByProjectIdAndKanbanLaneId(@Param("projectId") Long projectId, @Param("kanbanLaneId") Long kanbanLaneId);

    @Query("select kl from KanbanLane kl inner join kl.kanban where kl.kanban.project = :project")
    List<KanbanLane> findByProject(@Param("project") Project project);

    @Query("select kl from KanbanLane kl inner join Kanban k on kl.id=:kanbanLaneId and kl.kanban.id=k.id and k.project=:project")
    Optional<KanbanLane> findByIdAndProject(@Param("kanbanLaneId") Long kanbanLaneId, @Param("project") Project project);
}
