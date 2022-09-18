package com.projectboated.backend.utils.base.repository;

import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanban.repository.KanbanRepository;
import com.projectboated.backend.project.project.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;

public class KanbanRepositoryTest extends ProjectRepositoryTest {

    @Autowired
    protected KanbanRepository kanbanRepository;

    protected Kanban insertKanban(Project project) {
        return kanbanRepository.save(createKanban(project));
    }

}
