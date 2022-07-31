package com.projectboated.backend.domain.task.task.entity;

import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.projectboated.backend.domain.common.entity.BaseTimeEntity;
import com.projectboated.backend.domain.project.entity.Project;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
    private List<AccountTask> assignedAccounts = new ArrayList<>();

    @Builder
    public Task(Long id, String name, String description, LocalDateTime deadline) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }

    public void changeKanbanLane(KanbanLane kanbanLane) {
        this.kanbanLane = kanbanLane;
    }

    public void changeProject(Project project) {
        this.project = project;
    }
}
