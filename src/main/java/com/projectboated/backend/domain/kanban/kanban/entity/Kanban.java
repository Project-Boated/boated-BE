package com.projectboated.backend.domain.kanban.kanban.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.projectboated.backend.common.entity.BaseTimeEntity;
import com.projectboated.backend.domain.project.entity.Project;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Kanban extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kanban_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public Kanban(Project project) {
        changeProject(project);
    }

    @Builder
    public Kanban(Long id, Project project) {
        this.id = id;
        changeProject(project);
    }

    public void changeProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Kanban kanban = (Kanban) o;

        return id != null ? id.equals(kanban.id) : kanban.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
