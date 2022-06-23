package org.projectboated.backend.domain.kanban.repository;

import org.projectboated.backend.domain.kanban.entity.KanbanLane;
import org.projectboated.backend.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KanbanLaneRepository extends JpaRepository<KanbanLane, Long> {
    @Modifying
    @Query("delete from KanbanLane kl where kl.project=:project")
    void deleteByProject(@Param("project") Project project);

    @Query("select count(kl) from KanbanLane kl where kl.project=:project")
    Long countByProject(@Param("project") Project project);

    @Modifying
    @Query("delete from CustomKanbanLane ckl where ckl.project.id=:projectId")
    void deleteCustomLaneById(Long projectId);
}
