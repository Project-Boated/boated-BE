package com.projectboated.backend.domain.kanban.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.projectboated.backend.domain.common.entity.BaseTimeEntity;
import com.projectboated.backend.domain.project.entity.Project;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Kanban extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kanban_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kanban")
    private List<KanbanLane> lanes = new ArrayList<>();

    public Kanban(Project project) {
        this.project = project;
    }

    public void changeProject(Project project) {
        if(!this.project.equals(project)) {
            this.project = project;
            project.changeKanban(this);
        }
    }

    public void addLane(KanbanLane kanbanLane) {
        this.lanes.add(kanbanLane);
        kanbanLane.setKanban(this);
    }
}
