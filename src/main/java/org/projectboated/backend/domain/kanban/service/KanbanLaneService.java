package org.projectboated.backend.domain.kanban.service;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.account.repository.AccountRepository;
import org.projectboated.backend.domain.exception.ErrorCode;
import org.projectboated.backend.domain.kanban.entity.KanbanLane;
import org.projectboated.backend.domain.kanban.exception.KanbanLaneAlreadyExists5;
import org.projectboated.backend.domain.kanban.exception.KanbanLaneSaveAccessDeniedException;
import org.projectboated.backend.domain.kanban.repository.KanbanLaneRepository;
import org.projectboated.backend.domain.kanban.repository.KanbanRepository;
import org.projectboated.backend.domain.project.entity.Project;
import org.projectboated.backend.domain.project.exception.ProjectNotFoundException;
import org.projectboated.backend.domain.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KanbanLaneService {
    private final ProjectRepository projectRepository;
    private final KanbanLaneRepository kanbanLaneRepository;

    @Transactional
    public void save(Account account, Long projectId, KanbanLane kanbanLane) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!isCaptain(account, project)) {
            throw new KanbanLaneSaveAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        if (kanbanLaneRepository.countByProject(project) >= 5) {
            throw new KanbanLaneAlreadyExists5(ErrorCode.KANBAN_LANE_EXISTS_UPPER_5);
        }

        kanbanLane.setProject(project);
        kanbanLane.setKanban(project.getKanban());
        kanbanLaneRepository.save(kanbanLane);
    }

    @Transactional
    public void deleteCustomLane(Account account, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!isCaptain(account, project)) {
            throw new KanbanLaneSaveAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        kanbanLaneRepository.deleteCustomLaneById(projectId);
    }

    private boolean isCaptain(Account account, Project project) {
        return project.getCaptain().getId() == account.getId();
    }
}
