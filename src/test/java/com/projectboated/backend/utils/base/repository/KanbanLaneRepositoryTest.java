package com.projectboated.backend.utils.base.repository;

import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.project.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
