package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanban.repository.KanbanRepository;
import com.projectboated.backend.domain.project.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;

public class KanbanRepositoryTest extends ProjectRepositoryTest {

    @Autowired
    protected KanbanRepository kanbanRepository;

    protected Kanban insertKanban(Project project) {
        return kanbanRepository.save(createKanban(project));
    }

}
