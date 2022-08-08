package com.projectboated.backend.domain.kanban.kanban.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanban.service.exception.KanbanAccessDeniedException;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.KanbanLaneChangeIndexDeniedException;
import com.projectboated.backend.domain.project.aop.OnlyCaptain;
import com.projectboated.backend.domain.project.aop.OnlyCaptainOrCrew;
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

    @OnlyCaptainOrCrew
    public Kanban findByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));
        return project.getKanban();
    }

    @Transactional
    @OnlyCaptainOrCrew
    public void changeKanbanLaneOrder(Long projectId, int originalIndex, int changeIndex) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));
        project.getKanban().changeKanbanLaneOrder(originalIndex, changeIndex);
    }
}
