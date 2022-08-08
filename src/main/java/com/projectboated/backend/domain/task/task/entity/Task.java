package com.projectboated.backend.domain.task.task.entity;

import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.task.task.service.dto.TaskUpdateRequest;
import com.projectboated.backend.domain.task.tasklike.entity.TaskLike;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return id != null ? id.equals(task.id) : task.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void changeKanbanLane(KanbanLane kanbanLane) {
        this.kanbanLane = kanbanLane;
    }

    public void changeProject(Project project) {
        this.project = project;
    }

    public void update(TaskUpdateRequest request) {
        if (request.getName() != null) {
            this.name = request.getName();
        }
        if (request.getDescription() != null) {
            this.description = request.getDescription();
        }
        if (request.getDeadline() != null) {
            this.deadline = request.getDeadline();
        }
    }
}
