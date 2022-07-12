package com.projectboated.backend.domain.kanban.kanbanlane.repository;

import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KanbanLaneRepository extends JpaRepository<KanbanLane, Long> {

    Optional<KanbanLane> findByKanbanAndName(Kanban kanban, String name);

    @Query("select count(kl) from KanbanLane kl where kl.kanban=:kanban")
    Long countByKanban(@Param("kanban") Kanban kanban);

    @Modifying
    @Query("delete from KanbanLane kl where kl.kanban=:kanban")
    int deleteByKanban(@Param("kanban") Kanban kanban);

    @Modifying
    @Query("delete from CustomKanbanLane ckl where ckl.kanban=:kanban")
    void deleteCustomLaneByKanban(@Param("kanban") Kanban kanban);

}
