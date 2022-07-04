package com.projectboated.backend.domain.kanban.kanbanlane.entity;

import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.projectboated.backend.domain.common.entity.BaseTimeEntity;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.task.entity.Task;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class KanbanLane extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kanban_lane_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_id")
    private Kanban kanban;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kanbanLane")
    private List<Task> tasks = new ArrayList<>();

    protected KanbanLane(String name, Project project, Kanban kanban) {
        this.name = name;
        this.project = project;
        this.kanban = kanban;
    }

    public void changeProject(Project project) {
        this.project = project;
    }

    public void changeKanban(Kanban kanban) {
        this.kanban = kanban;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
        task.changeKanbanLane(this);
    }
}
