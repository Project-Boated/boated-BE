package my.sleepydeveloper.projectcompass.domain.kanban;

import my.sleepydeveloper.projectcompass.domain.common.entity.BaseTimeEntity;
import my.sleepydeveloper.projectcompass.domain.project.Project;

import javax.persistence.*;

@Entity
public class Kanban extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kanban_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
}
