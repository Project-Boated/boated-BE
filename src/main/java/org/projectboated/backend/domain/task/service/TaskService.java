package org.projectboated.backend.domain.task.service;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.account.repository.AccountRepository;
import org.projectboated.backend.domain.exception.ErrorCode;
import org.projectboated.backend.domain.kanban.entity.KanbanLane;
import org.projectboated.backend.domain.kanban.exception.KanbanLaneNotFoundException;
import org.projectboated.backend.domain.kanban.repository.KanbanLaneRepository;
import org.projectboated.backend.domain.project.entity.Project;
import org.projectboated.backend.domain.project.exception.ProjectNotFoundException;
import org.projectboated.backend.domain.project.repository.ProjectRepository;
import org.projectboated.backend.domain.project.service.ProjectService;
import org.projectboated.backend.domain.task.entity.Task;
import org.projectboated.backend.domain.task.exception.TaskSaveAccessDeniedException;
import org.projectboated.backend.domain.task.repository.TaskRepository;
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
