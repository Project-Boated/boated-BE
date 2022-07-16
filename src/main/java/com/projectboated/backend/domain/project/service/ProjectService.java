package com.projectboated.backend.domain.project.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.kanban.kanban.entity.Kanban;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.DefaultKanbanLane;
import com.projectboated.backend.domain.kanban.kanbanlane.entity.KanbanLaneType;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.repository.ProjectRepository;
import com.projectboated.backend.domain.project.service.condition.GetMyProjectsCond;
import com.projectboated.backend.domain.project.service.condition.ProjectUpdateCond;
import com.projectboated.backend.domain.project.service.dto.MyProjectsDto;
import com.projectboated.backend.domain.project.service.exception.ProjectDeleteAccessDeniedException;
import com.projectboated.backend.domain.project.service.exception.ProjectNameSameInAccountException;
import com.projectboated.backend.domain.project.service.exception.ProjectNotFoundException;
import com.projectboated.backend.domain.project.service.exception.ProjectUpdateAccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public Project save(Project project) {
        if (projectRepository.existsByNameAndCaptain(project.getName(), project.getCaptain())) {
            throw new ProjectNameSameInAccountException(ErrorCode.PROJECT_NAME_EXISTS_IN_ACCOUNT);
        }

        Kanban newKanban = new Kanban(project);
        project.changeKanban(newKanban);

        for (KanbanLaneType kanbanLaneType : KanbanLaneType.values()) {
            String name = kanbanLaneType.name();
            newKanban.addKanbanLane(new DefaultKanbanLane(name, newKanban));
        }

        return projectRepository.save(project);
    }

    @Transactional
    public void update(Account account, Long projectId, ProjectUpdateCond cond) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (project.getCaptain().getId() != account.getId()) {
            throw new ProjectUpdateAccessDeniedException(ErrorCode.PROJECT_UPDATE_ACCESS_DENIED);
        }

        if (cond.getName() != null &&
                !cond.getName().equals(project.getName()) &&
                projectRepository.existsByNameAndCaptain(cond.getName(), account)) {
            throw new ProjectNameSameInAccountException(ErrorCode.PROJECT_NAME_EXISTS_IN_ACCOUNT);
        }

        project.update(cond);
    }

    @Transactional
    public void delete(Account account, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (project.getCaptain().getId() != account.getId()) {
            throw new ProjectDeleteAccessDeniedException(ErrorCode.PROJECT_DELETE_ACCESS_DENIED);
        }

        projectRepository.delete(project);
    }

    public Project findById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));
    }

    public MyProjectsDto getMyProjects(Account account, GetMyProjectsCond cond) {
        List<Project> projects = projectRepository.getMyProjects(account, cond);
        return MyProjectsDto.builder()
                .page(cond.getPageable().getPageNumber())
                .size(projects.size())
                .hasNext(projects.size() == cond.getPageable().getPageSize())
                .projects(projects)
                .build();
    }
}
