package com.projectboated.backend.domain.task.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.service.exception.KanbanLaneNotFoundException;
import com.projectboated.backend.domain.kanban.kanbanlane.repository.KanbanLaneRepository;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.task.entity.Task;
import com.projectboated.backend.domain.task.service.exception.TaskSaveAccessDeniedException;
import com.projectboated.backend.domain.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.project.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final KanbanLaneRepository kanbanLaneRepository;
    private final ProjectService projectService;

    @Transactional
    public Task save(Account account, Long projectId, Task task) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!projectService.isCaptain(findAccount, project) &&
                !projectService.isCrew(findAccount, projectId)) {
            throw new TaskSaveAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        KanbanLane kanbanLane = kanbanLaneRepository.findByProjectAndName(project, "READY")
                .orElseThrow(() -> new KanbanLaneNotFoundException(ErrorCode.KANBAN_LANE_NOT_FOUND));

        task.changeProject(project);
        task.changeKanbanLane(kanbanLane);

        return taskRepository.save(task);
    }
}
