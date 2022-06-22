package my.sleepydeveloper.projectcompass.domain.kanban.repository;

import my.sleepydeveloper.projectcompass.domain.kanban.entity.KanbanLane;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KanbanLaneRepository extends JpaRepository<KanbanLane, Long> {
}
