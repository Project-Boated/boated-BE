package com.projectboated.backend.utils.base.base;

import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.project.project.entity.Project;

import java.util.ArrayList;
import java.util.List;

import static com.projectboated.backend.utils.data.BasicDataKanbanLane.KANBAN_LANE_NAME;

public class BaseKanbanLaneTest extends BaseKanbanTest {

    protected KanbanLane createKanbanLane(Project project, Kanban kanban) {
        return KanbanLane.builder()
                .kanban(kanban)
                .project(project)
                .name(KANBAN_LANE_NAME)
                .build();
    }

    protected KanbanLane createKanbanLane(Long id, Project project, Kanban kanban) {
        return KanbanLane.builder()
                .id(id)
                .kanban(kanban)
                .project(project)
                .name(KANBAN_LANE_NAME)
                .build();
    }

    protected KanbanLane createKanbanLane(Project project, Kanban kanban, String name) {
        return KanbanLane.builder()
                .kanban(kanban)
                .project(project)
                .name(name)
                .build();
    }

    protected KanbanLane createKanbanLane(Long id, Project project, Kanban kanban, String name) {
        return KanbanLane.builder()
                .id(id)
                .project(project)
                .kanban(kanban)
                .name(name)
                .build();
    }

    protected List<KanbanLane> createKanbanLanes(Long baseId, Project project, Kanban kanban, int count) {
        List<KanbanLane> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(KanbanLane.builder()
                    .id(baseId + i)
                    .project(project)
                    .kanban(kanban)
                    .name(KANBAN_LANE_NAME + i)
                    .build());
        }
        return result;
    }

    protected List<KanbanLane> createKanbanLanes(Project project, Kanban kanban, int count) {
        List<KanbanLane> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(KanbanLane.builder()
                    .project(project)
                    .kanban(kanban)
                    .name(KANBAN_LANE_NAME + i)
                    .build());
        }
        return result;
    }

}
