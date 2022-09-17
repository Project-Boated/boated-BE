package com.projectboated.backend.domain.task.task.entity;

import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.task.task.service.dto.TaskUpdateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.projectboated.backend.common.entity.BaseTimeEntity;
import com.projectboated.backend.domain.project.entity.Project;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    private String name;

    private String description;

    private LocalDateTime deadline;

    @Column(name = "task_index")
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_lane_id")
    private KanbanLane kanbanLane;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public Task(Long id, String name, String description, LocalDateTime deadline, Integer order) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.order = order;
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

    public void changeOrder(int order) {
        this.order = order;
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
}
