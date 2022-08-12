package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.project.entity.Project;

import java.util.List;
import java.util.stream.IntStream;

import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_ID;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_NAME;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;

public class BaseKanbanLaneTest extends BaseKanbanTest{

    protected Project createProjectAnd4Lanes(Account account) {
        Project project = Project.builder()
                .id(PROJECT_ID)
                .captain(account)
                .build();
        Kanban kanban = createKanban(project);
        addKanbanLane(kanban, 4);
        return project;
    }

    protected List<KanbanLane> addKanbanLane(Kanban kanban, int count) {
        return IntStream.range(0, count)
                .mapToObj((i) -> KanbanLane.builder()
                        .id(KANBAN_LANE_ID+i)
                        .name(KANBAN_LANE_NAME + i)
                        .build())
                .peek(kanban::addKanbanLane)
                .toList();
    }

    protected KanbanLane addKanbanLane(Kanban kanban) {
        KanbanLane kanbanLane = KanbanLane.builder()
                .id(KANBAN_LANE_ID)
                .name(KANBAN_LANE_NAME)
                .build();
        kanban.addKanbanLane(kanbanLane);
        return kanbanLane;
    }

    protected KanbanLane createKanbanLane(Kanban kanban) {
        return KanbanLane.builder()
                .kanban(kanban)
                .name(KANBAN_LANE_NAME)
                .build();
    }

}
