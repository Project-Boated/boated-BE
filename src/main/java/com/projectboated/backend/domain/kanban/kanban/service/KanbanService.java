package com.projectboated.backend.domain.kanban.kanban.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanban.service.exception.KanbanAccessDeniedException;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.KanbanLaneChangeIndexDeniedException;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.AccountProjectService;
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
    private final AccountProjectService accountProjectService;

    public Kanban find(Account account, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!(project.getCaptain().getId() == account.getId()) &&
                !accountProjectService.isCrew(project, account)) {
            throw new KanbanAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        return project.getKanban();
    }

    @Transactional
    public void changeKanbanLaneOrder(Account account, Long projectId, int originalIndex, int changeIndex) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.isCaptain(account) &&
                !accountProjectService.isCrew(project, account)) {
            throw new KanbanLaneChangeIndexDeniedException(ErrorCode.KANBAN_LANE_CHANGE_INDEX_ACCESS_DENIED);
        }

        project.getKanban().changeKanbanLaneOrder(originalIndex, changeIndex);
    }
}
