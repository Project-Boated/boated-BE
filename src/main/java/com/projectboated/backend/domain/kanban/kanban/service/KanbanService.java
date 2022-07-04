package com.projectboated.backend.domain.kanban.kanban.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanban.service.exception.KanbanAccessDeniedException;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.kanban.kanban.repository.KanbanRepository;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.project.service.ProjectService;
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
