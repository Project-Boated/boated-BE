package my.sleepydeveloper.projectcompass.domain.task.entity;

import my.sleepydeveloper.projectcompass.domain.common.entity.BaseTimeEntity;
import my.sleepydeveloper.projectcompass.domain.kanban.entity.KanbanLane;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Task extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    private LocalDateTime deadline;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_lane_id")
    private KanbanLane kanbanLane;
}
