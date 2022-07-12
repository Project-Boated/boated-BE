package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.common.data.BasicDataKanbanLane;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.CustomKanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.DefaultKanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLaneType;
import com.projectboated.backend.domain.kanban.kanbanlane.repository.KanbanLaneRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class KanbanLaneRepositoryTest extends KanbanRepositoryTest{

    @Autowired
    protected KanbanLaneRepository kanbanLaneRepository;

    protected void insertDefaultKanbanLanes(Kanban kanban) {
        for (KanbanLaneType klt : KanbanLaneType.values()) {
            kanbanLaneRepository.save(DefaultKanbanLane.builder()
                    .kanban(kanban)
                    .name(klt.name())
                    .build());
        }
    }

    protected KanbanLane insertDefaultCustomKanbanLane(Kanban kanban) {
        return kanbanLaneRepository.save(CustomKanbanLane.builder()
                .kanban(kanban)
                .name(BasicDataKanbanLane.KANBAN_LANE_NAME)
                .build());
    }

    protected KanbanLane insertCustomKanbanLane(Kanban kanban, String name) {
        return kanbanLaneRepository.save(CustomKanbanLane.builder()
                .kanban(kanban)
                .name(name)
                .build());
    }


}
