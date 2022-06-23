package org.projectboated.backend.domain.kanban.service;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.exception.ErrorCode;
import org.projectboated.backend.domain.kanban.entity.Kanban;
import org.projectboated.backend.domain.kanban.exception.KanbanAccessDeniedException;
import org.projectboated.backend.domain.kanban.repository.KanbanRepository;
import org.projectboated.backend.domain.project.entity.Project;
import org.projectboated.backend.domain.project.exception.ProjectNotFoundException;
import org.projectboated.backend.domain.project.repository.ProjectRepository;
import org.projectboated.backend.domain.project.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KanbanService {

    private final KanbanRepository kanbanRepository;
    private final ProjectRepository projectRepository;

    private final ProjectService projectService;

    public Kanban find(Account account, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!projectService.isCaptain(account, project) &&
                !projectService.isCrew(account, projectId)) {
            throw new KanbanAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        return project.getKanban();
    }
}
