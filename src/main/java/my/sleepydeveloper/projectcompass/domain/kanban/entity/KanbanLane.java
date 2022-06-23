package my.sleepydeveloper.projectcompass.domain.kanban.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.common.entity.BaseTimeEntity;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KanbanLane extends BaseTimeEntity {

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

    @Builder
    public KanbanLane(String name, Project project, Kanban kanban) {
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
