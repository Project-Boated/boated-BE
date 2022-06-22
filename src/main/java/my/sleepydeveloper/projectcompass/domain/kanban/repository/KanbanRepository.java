package my.sleepydeveloper.projectcompass.domain.kanban.repository;

import my.sleepydeveloper.projectcompass.domain.kanban.entity.Kanban;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KanbanRepository extends JpaRepository<Kanban, Long> {
}
