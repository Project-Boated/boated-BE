package org.projectboated.backend.domain.kanban.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.projectboated.backend.domain.common.entity.BaseTimeEntity;
import org.projectboated.backend.domain.project.entity.Project;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class KanbanLane extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kanban_lane_id")
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_id")
    private Kanban kanban;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    protected KanbanLane(String name, Project project, Kanban kanban) {
        this.name = name;
        this.project = project;
        this.kanban = kanban;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setKanban(Kanban kanban) {
        this.kanban = kanban;
    }
}
