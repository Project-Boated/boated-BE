package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.common.data.BasicDataKanbanLane;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLaneType;
import com.projectboated.backend.domain.kanban.kanbanlane.repository.KanbanLaneRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class KanbanLaneRepositoryTest extends KanbanRepositoryTest{

    @Autowired
    protected KanbanLaneRepository kanbanLaneRepository;

    protected void insertKanbanLanes(Kanban kanban) {
        for (KanbanLaneType klt : KanbanLaneType.values()) {
            kanbanLaneRepository.save(KanbanLane.builder()
                    .kanban(kanban)
                    .name(klt.name())
                    .build());
        }
    }

    protected KanbanLane insertKanbanLane(Kanban kanban) {
        return kanbanLaneRepository.save(KanbanLane.builder()
                .kanban(kanban)
                .name(BasicDataKanbanLane.KANBAN_LANE_NAME)
                .build());
    }

    protected KanbanLane insertKanbanLane(Kanban kanban, String name) {
        return kanbanLaneRepository.save(KanbanLane.builder()
                .kanban(kanban)
                .name(name)
                .build());
    }


}
