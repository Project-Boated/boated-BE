package com.projectboated.backend.domain.kanban.kanbanlane.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.domain.kanban.kanbanlane.service.dto.ChangeTaskOrderRequest;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.KanbanLaneAlreadyExists5;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.KanbanLaneSaveAccessDeniedException;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.TaskChangeIndexDeniedException;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.AccountProjectService;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
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
    public void createNewLine(Long accountId, Long projectId, String name) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if(!project.isCaptain(account)) {
            throw new KanbanLaneSaveAccessDeniedException(ErrorCode.PROJECT_ONLY_CAPTAIN);
        }

        if (kanbanLaneRepository.countByKanban(project.getKanban()) >= 5) {
            throw new KanbanLaneAlreadyExists5(ErrorCode.KANBAN_LANE_EXISTS_UPPER_5);
        }

        KanbanLane kanbanLane = KanbanLane.builder()
                .name(name)
                .build();
        kanbanLaneRepository.save(kanbanLane);

        project.getKanban().addKanbanLane(kanbanLane);
    }

    @Transactional
    public void deleteCustomLane(Long accountId, Long projectId, Long kanbanLaneId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if(!project.isCaptain(account)) {
            throw new KanbanLaneSaveAccessDeniedException(ErrorCode.PROJECT_ONLY_CAPTAIN);
        }

        kanbanLaneRepository.deleteById(kanbanLaneId);
    }

    @Transactional
    public void changeTaskOrder(Long accountId, ChangeTaskOrderRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.isCaptain(account) &&
                !accountProjectService.isCrew(project, account)) {
            throw new TaskChangeIndexDeniedException(ErrorCode.PROJECT_ONLY_CAPTAIN_OR_CREW);
        }

        project.getKanban().changeTaskOrder(request);
    }
}
