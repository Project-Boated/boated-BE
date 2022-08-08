package com.projectboated.backend.domain.kanban.kanbanlane.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.domain.kanban.kanbanlane.service.dto.ChangeTaskOrderRequest;
import com.projectboated.backend.domain.kanban.kanbanlane.service.dto.KanbanLaneUpdateRequest;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.*;
import com.projectboated.backend.domain.project.aop.OnlyCaptainOrCrew;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.ProjectService;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.task.task.entity.Task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KanbanLaneService {
    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final KanbanLaneRepository kanbanLaneRepository;
    private final ProjectService projectService;

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
    public void deleteKanbanLane(Long accountId, Long projectId, Long kanbanLaneId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if(!project.isCaptain(account)) {
            throw new KanbanLaneSaveAccessDeniedException(ErrorCode.PROJECT_ONLY_CAPTAIN);
        }

        if (!isInProject(kanbanLaneId, project)) {
            throw new KanbanLaneNotInProjectException(ErrorCode.KANBAN_LANE_NOT_IN_PROJECT);
        }

        kanbanLaneRepository.deleteById(kanbanLaneId);
    }

    @Transactional
    public void changeTaskOrder(Long accountId, ChangeTaskOrderRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!projectService.isCaptainOrCrew(project, account)) {
            throw new TaskChangeOrderAccessDeniedException(ErrorCode.PROJECT_ONLY_CAPTAIN_OR_CREW);
        }
    
        KanbanLane originalKanbanLane = kanbanLaneRepository.findByProjectIdAndKanbanLaneId(project.getId(), request.originalLaneId())
                                                    .orElseThrow(KanbanLaneNotFoundException::new);
        KanbanLane changeKanbanLane = kanbanLaneRepository.findByProjectIdAndKanbanLaneId(project.getId(), request.changeLaneId())
                                                    .orElseThrow(KanbanLaneNotFoundException::new);
    
        Task task = originalKanbanLane.removeTask(request.originalTaskIndex());
        changeKanbanLane.addTask(request.changeTaskIndex(), task);
    }

    @Transactional
    public void updateKanbanLane(Long accountId, KanbanLaneUpdateRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!project.isCaptain(account)) {
            throw new KanbanLaneUpdateAccessDeniedException(ErrorCode.PROJECT_ONLY_CAPTAIN);
        }

        KanbanLane kanbanLane = kanbanLaneRepository.findByProjectIdAndKanbanLaneId(request.getProjectId(), request.getKanbanLaneId())
                .orElseThrow(KanbanLaneNotFoundException::new);

        kanbanLane.update(request);
    }

    private boolean isInProject(KanbanLane kanbanLane, Project project) {
        for (KanbanLane lane : project.getKanban().getLanes()) {
            if (lane.getId() == kanbanLane.getId()) {
                return true;
            }
        }
        return false;
    }

    private boolean isInProject(Long kanbanLaneId, Project project) {
        for (KanbanLane lane : project.getKanban().getLanes()) {
            if (Objects.equals(lane.getId(), kanbanLaneId)) {
                return true;
            }
        }
        return false;
    }

    @OnlyCaptainOrCrew
    public List<KanbanLane> getLanes(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        return kanbanLaneRepository.findByProject(project);
    }
}
