package my.sleepydeveloper.projectcompass.domain.kanban.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.common.entity.BaseTimeEntity;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Kanban extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kanban_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public Kanban(Project project) {
        this.project = project;
    }

    public void changeProject(Project project) {
        if(!this.project.equals(project)) {
            this.project = project;
            project.changeKanban(this);
        }
    }
}
