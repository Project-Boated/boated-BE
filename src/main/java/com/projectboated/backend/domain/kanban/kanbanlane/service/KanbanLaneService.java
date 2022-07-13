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

import java.util.List;

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
    public void changeKanbanLaneIndex(Account account, Long projectId, int originalIndex, int changeIndex) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.isCaptain(account) &&
                !accountProjectService.isCrew(project, account)) {
            throw new KanbanLaneChangeIndexDeniedException(ErrorCode.KANBAN_LANE_CHANGE_INDEX_DENIED);
        }

        List<KanbanLane> lanes = project.getKanban().getLanes();
        if (originalIndex < 0 || originalIndex >= lanes.size()) {
            throw new KanbanLaneOriginalIndexOutOfBoundsException(ErrorCode.KANBAN_LANE_ORIGINAL_INDEX_OUT_OF_BOUNDS);
        }
        if (changeIndex < 0 || changeIndex >= lanes.size()) {
            throw new KanbanLaneChangeIndexOutOfBoundsException(ErrorCode.KANBAN_LANE_CHANGE_INDEX_OUT_OF_BOUNDS);
        }

        KanbanLane targetKanbanLane = lanes.remove(originalIndex);
        if (changeIndex == lanes.size()) {
            lanes.add(targetKanbanLane);
        } else {
            lanes.add(changeIndex, targetKanbanLane);
        }
    }
}
