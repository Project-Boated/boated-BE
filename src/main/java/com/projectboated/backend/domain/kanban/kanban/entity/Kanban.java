package com.projectboated.backend.domain.kanban.kanban.entity;

import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.projectboated.backend.domain.common.entity.BaseTimeEntity;
import com.projectboated.backend.domain.project.entity.Project;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Kanban extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kanban_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kanban",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    @OrderColumn(name = "kanban_lane_index")
    private List<KanbanLane> lanes = new ArrayList<>();

    @Builder
    public Kanban(Project project) {
        changeProject(project);
    }

    public void changeProject(Project project) {
        if (this.project==null || !this.project.equals(project)) {
            this.project = project;
            project.changeKanban(this);
        }
    }

    public void addKanbanLane(KanbanLane kanbanLane) {
        this.lanes.add(kanbanLane);
        kanbanLane.changeKanban(this);
    }
}
