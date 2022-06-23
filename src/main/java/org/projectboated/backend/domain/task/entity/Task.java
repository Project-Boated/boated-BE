package org.projectboated.backend.domain.task.entity;

import lombok.Builder;
import org.projectboated.backend.domain.common.entity.BaseTimeEntity;
import org.projectboated.backend.domain.kanban.entity.KanbanLane;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Task extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    private String name;

    private String description;

    private LocalDateTime deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_lane_id")
    private KanbanLane kanbanLane;

    @Builder
    public Task(String name, String description, LocalDateTime deadline) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }

    public void setKanbanLane(KanbanLane kanbanLane) {
        this.kanbanLane = kanbanLane;
    }
}
