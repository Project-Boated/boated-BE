package com.projectboated.backend.domain.kanban.kanbanlane.repository;

import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;

import com.projectboated.backend.domain.project.entity.Project;
import org.checkerframework.framework.qual.PreconditionAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface KanbanLaneRepository extends JpaRepository<KanbanLane, Long> {

    @Query("select count(kl) from KanbanLane kl where kl.kanban=:kanban")
    Long countByKanban(@Param("kanban") Kanban kanban);
	
    @Query("select kl from KanbanLane kl inner join Kanban k on kl.id=:kanbanLaneId and kl.kanban.id=k.id and k.project.id=:projectId")
	Optional<KanbanLane> findByProjectIdAndKanbanLaneId(@Param("projectId") Long projectId, @Param("kanbanLaneId") Long kanbanLaneId);

    @Query("select kl from KanbanLane kl inner join kl.kanban where kl.kanban.project = :project order by kl.order")
    List<KanbanLane> findByProject(@Param("project") Project project);

    @Modifying
    @Query("delete from KanbanLane kl where kl.project=:project")
    void deleteByProject(@Param("project") Project project);

    @Query("select max(kl.order) from KanbanLane kl where kl.project=:project")
    Integer findHighestOrder(@Param("project") Project project);

    @Query("select kl from KanbanLane kl where kl.project=:project and kl.order=0")
    Optional<KanbanLane> findByProjectAndFirstOrder(@Param("project") Project project);
}
