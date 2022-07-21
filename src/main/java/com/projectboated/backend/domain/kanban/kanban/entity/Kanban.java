package com.projectboated.backend.domain.kanban.kanban.entity;

import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanban.entity.exception.KanbanLaneChangeIndexOutOfBoundsException;
import com.projectboated.backend.domain.kanban.kanban.entity.exception.KanbanLaneOriginalIndexOutOfBoundsException;
import com.projectboated.backend.domain.kanban.kanbanlane.service.dto.ChangeTaskOrderRequest;
import com.projectboated.backend.domain.task.entity.Task;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.projectboated.backend.domain.common.entity.BaseTimeEntity;
import com.projectboated.backend.domain.project.entity.Project;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Kanban extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kanban_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kanban",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    @OrderColumn(name = "kanban_lane_index")
    private List<KanbanLane> lanes = new ArrayList<>();

    @Builder
    public Kanban(Project project) {
        changeProject(project);
    }

    public void changeProject(Project project) {
        if (this.project==null || !this.project.equals(project)) {
            this.project = project;
            project.changeKanban(this);
        }
    }

    public void addKanbanLane(KanbanLane kanbanLane) {
        this.lanes.add(kanbanLane);
        kanbanLane.changeKanban(this);
    }

    public void changeKanbanLaneOrder(int originalIndex, int changeIndex) {
        if (originalIndex < 0 || originalIndex >= lanes.size()) {
            throw new KanbanLaneOriginalIndexOutOfBoundsException(ErrorCode.KANBAN_LANE_ORIGINAL_INDEX_OUT_OF_BOUNDS);
        }
        if (changeIndex < 0 || changeIndex >= lanes.size()) {
            throw new KanbanLaneChangeIndexOutOfBoundsException(ErrorCode.KANBAN_LANE_CHANGE_INDEX_OUT_OF_BOUNDS);
        }

        KanbanLane targetKanbanLane = lanes.remove(originalIndex);
        if (changeIndex == lanes.size()) {
            lanes.add(targetKanbanLane);
        } else {
            lanes.add(changeIndex, targetKanbanLane);
        }
    }

    public void changeTaskOrder(ChangeTaskOrderRequest request) {
        int originalLaneIndex = request.originalLaneIndex();
        int changeLaneIndex = request.changeLaneIndex();

        if (originalLaneIndex < 0 || originalLaneIndex >= lanes.size()) {
            throw new KanbanLaneOriginalIndexOutOfBoundsException(ErrorCode.KANBAN_LANE_ORIGINAL_INDEX_OUT_OF_BOUNDS);
        }
        if (changeLaneIndex < 0 || changeLaneIndex >= lanes.size()) {
            throw new KanbanLaneChangeIndexOutOfBoundsException(ErrorCode.KANBAN_LANE_CHANGE_INDEX_OUT_OF_BOUNDS);
        }

        Task removed = lanes.get(originalLaneIndex).removeTask(request.originalTaskIndex());
        lanes.get(changeLaneIndex).addTask(request.changeTaskIndex(), removed);
    }
}
