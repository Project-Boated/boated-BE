package com.projectboated.backend.domain.kanban.kanbanlane.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.*;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.AccountProjectService;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KanbanLaneService {
    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final KanbanLaneRepository kanbanLaneRepository;
    private final AccountProjectService accountProjectService;

    @Transactional
    public void save(Account account, Long projectId, KanbanLane kanbanLane) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!isCaptain(account, project)) {
            throw new KanbanLaneSaveAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        if (kanbanLaneRepository.countByKanban(project.getKanban()) >= 5) {
            throw new KanbanLaneAlreadyExists5(ErrorCode.KANBAN_LANE_EXISTS_UPPER_5);
        }

        kanbanLaneRepository.save(kanbanLane);
        project.getKanban().addKanbanLane(kanbanLane);
    }

    @Transactional
    public void deleteCustomLane(Account account, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!isCaptain(account, project)) {
            throw new KanbanLaneSaveAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        kanbanLaneRepository.deleteCustomLaneByKanban(project.getKanban());
    }

    private boolean isCaptain(Account account, Project project) {
        return project.getCaptain().getId() == account.getId();
    }

    @Transactional
    public void changeTaskOrder(Account account, Long projectId, Long kanbanLaneId, int originalIndex, int changeIndex) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.isCaptain(account) &&
                !accountProjectService.isCrew(project, account)) {
            throw new TaskChangeIndexDeniedException(ErrorCode.TASK_CHANGE_INDEX_ACCESS_DENIED);
        }

        KanbanLane kanbanLane = kanbanLaneRepository.findById(kanbanLaneId)
                .orElseThrow(() -> new KanbanLaneNotFoundException(ErrorCode.KANBAN_LANE_NOT_FOUND));

        if (project.getId() != kanbanLane.getKanban().getProject().getId()) {
            throw new ProjectDoesntHaveKanbanLane(ErrorCode.PROJECT_DOESNT_HAVE_KANBAN_LANE);
        }

        kanbanLane.changeTaskOrder(originalIndex, changeIndex);
    }
}
