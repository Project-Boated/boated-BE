package com.example.projectcompass.domain.task;

import com.example.projectcompass.domain.common.Entity.BaseTimeEntity;
import com.example.projectcompass.domain.kanban.KanbanLane;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Task extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "task_id")
    private Long id;

    private LocalDateTime deadline;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_lane_id")
    private KanbanLane kanbanLane;
}
