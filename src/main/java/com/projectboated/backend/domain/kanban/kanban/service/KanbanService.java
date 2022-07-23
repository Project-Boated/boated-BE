package com.projectboated.backend.domain.kanban.kanban.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanban.service.exception.KanbanAccessDeniedException;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.KanbanLaneChangeIndexDeniedException;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.project.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KanbanService {

    private final ProjectService projectService;
    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;

    public Kanban findByProjectId(Long accountId, Long projectId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if(!projectService.isCaptainOrCrew(project, account)) {
            throw new KanbanAccessDeniedException(ErrorCode.PROJECT_ONLY_CAPTAIN_OR_CREW);
        }

        return project.getKanban();
    }

    @Transactional
    public void changeKanbanLaneOrder(Long accountId, Long projectId, int originalIndex, int changeIndex) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!projectService.isCaptainOrCrew(project, account)) {
            throw new KanbanLaneChangeIndexDeniedException(ErrorCode.PROJECT_ONLY_CAPTAIN_OR_CREW);
        }

        project.getKanban().changeKanbanLaneOrder(originalIndex, changeIndex);
    }
}
