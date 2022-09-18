package com.projectboated.backend.kanban.kanban.service;

import com.projectboated.backend.project.aop.OnlyCaptainOrCrew;
import com.projectboated.backend.project.entity.Project;
import com.projectboated.backend.project.repository.ProjectRepository;
import com.projectboated.backend.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.kanban.kanban.entity.Kanban;
import com.projectboated.backend.kanban.kanban.repository.KanbanRepository;
import com.projectboated.backend.kanban.kanban.service.exception.KanbanNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KanbanService {

    private final ProjectRepository projectRepository;
    private final KanbanRepository kanbanRepository;

    @OnlyCaptainOrCrew
    public Kanban findByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        return kanbanRepository.findByProject(project)
                .orElseThrow(KanbanNotFoundException::new);
    }
}
