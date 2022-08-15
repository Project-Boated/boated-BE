package com.projectboated.backend.common.basetest.repository;

import java.util.List;

import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.repository.KanbanLaneRepository;

import com.projectboated.backend.domain.project.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;

public class KanbanLaneRepositoryTest extends KanbanRepositoryTest {

    @Autowired
    protected KanbanLaneRepository kanbanLaneRepository;

    protected KanbanLane insertKanbanLane(Project project, Kanban kanban) {
        return kanbanLaneRepository.save(createKanbanLane(project, kanban));
    }

    protected KanbanLane insertKanbanLane(Project project, Kanban kanban, String name) {
        return kanbanLaneRepository.save(createKanbanLane(project, kanban, name));
    }

    protected List<KanbanLane> insertKanbanLanes(Project project, Kanban kanban, int count) {
        return kanbanLaneRepository.saveAll(createKanbanLanes(project, kanban, count));
    }

}
