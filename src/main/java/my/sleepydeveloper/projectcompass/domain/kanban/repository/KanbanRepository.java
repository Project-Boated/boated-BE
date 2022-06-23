package my.sleepydeveloper.projectcompass.domain.kanban.repository;

import my.sleepydeveloper.projectcompass.domain.kanban.entity.Kanban;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KanbanRepository extends JpaRepository<Kanban, Long> {
    @Modifying
    @Query("delete from Kanban k where k.project=:project")
    void deleteByProject(@Param("project") Project project);

    Optional<Kanban> findByProject(Long projectId);
}
