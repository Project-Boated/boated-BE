package org.projectboated.backend.domain.kanban.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.projectboated.backend.domain.project.entity.Project;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("default")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultKanbanLane extends KanbanLane{

    @Builder
    public DefaultKanbanLane(String name, Project project, Kanban kanban) {
        super(name, project, kanban);
    }
}
