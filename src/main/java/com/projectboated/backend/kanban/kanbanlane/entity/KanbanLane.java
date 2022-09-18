package com.projectboated.backend.kanban.kanbanlane.entity;

import com.projectboated.backend.common.entity.BaseTimeEntity;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanbanlane.service.dto.KanbanLaneUpdateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KanbanLane extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kanban_lane_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kanban_id")
    private Kanban kanban;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "kanban_lane_index")
    private Integer order;

    @Builder
    public KanbanLane(Long id, String name, Integer order, Project project, Kanban kanban) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.kanban = kanban;
        this.project = project;
    }

    public void update(KanbanLaneUpdateRequest request) {
        if (request.getName() != null) {
            this.name = request.getName();
        }
        if (request.getOrder() != null) {
            this.order = request.getOrder();
        }
        if (request.getProject() != null) {
            this.project = request.getProject();
        }
        if (request.getKanban() != null) {
            this.kanban = request.getKanban();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KanbanLane that = (KanbanLane) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void changeOrder(int order) {
        this.order = order;
    }
}
