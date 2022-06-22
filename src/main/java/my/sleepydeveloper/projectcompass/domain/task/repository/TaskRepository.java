package my.sleepydeveloper.projectcompass.domain.task.repository;

import my.sleepydeveloper.projectcompass.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
