package com.projectboated.backend.domain.kanban.kanbanlane.entity;

import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("default")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultKanbanLane extends KanbanLane{

    @Builder
    public DefaultKanbanLane(String name, Kanban kanban) {
        super(name, kanban);
    }
}
