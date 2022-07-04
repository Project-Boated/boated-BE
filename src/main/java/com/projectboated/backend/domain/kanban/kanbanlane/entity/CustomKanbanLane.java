package com.projectboated.backend.domain.kanban.kanbanlane.entity;

import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import com.projectboated.backend.domain.project.entity.Project;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("custom")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomKanbanLane extends KanbanLane{

    @Builder
    public CustomKanbanLane(String name, Project project, Kanban kanban) {
        super(name, project, kanban);
    }
}
