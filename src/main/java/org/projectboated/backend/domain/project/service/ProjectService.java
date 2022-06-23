package org.projectboated.backend.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.account.exception.AccountNotFoundException;
import org.projectboated.backend.domain.account.repository.AccountRepository;
import org.projectboated.backend.domain.accountproject.entity.AccountProject;
import org.projectboated.backend.domain.accountproject.repository.AccountProjectRepository;
import org.projectboated.backend.domain.exception.ErrorCode;
import org.projectboated.backend.domain.kanban.entity.DefaultKanbanLane;
import org.projectboated.backend.domain.kanban.entity.Kanban;
import org.projectboated.backend.domain.kanban.entity.KanbanLane;
import org.projectboated.backend.domain.kanban.entity.KanbanLaneType;
import org.projectboated.backend.domain.kanban.repository.KanbanLaneRepository;
import org.projectboated.backend.domain.kanban.repository.KanbanRepository;
import org.projectboated.backend.domain.project.dto.ProjectFindCrewsAccessDeniedException;
import org.projectboated.backend.domain.project.entity.Project;
import org.projectboated.backend.domain.project.exception.*;
import org.projectboated.backend.domain.project.repository.ProjectRepository;
import org.projectboated.backend.domain.project.vo.ProjectUpdateCondition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AccountRepository accountRepository;
    private final KanbanRepository kanbanRepository;
    private final KanbanLaneRepository kanbanLaneRepository;
    private final AccountProjectRepository accountProjectRepository;

    @Transactional
    public Project save(Project project) {
        if (projectRepository.existsByNameAndCaptain(project.getName(), project.getCaptain())) {
            throw new ProjectNameSameInAccountException(ErrorCode.PROJECT_NAME_EXISTS_IN_ACCOUNT);
        }

        Project newProject = projectRepository.save(project);

        Kanban newKanban = new Kanban(newProject);
        kanbanRepository.save(newKanban);

        for (KanbanLaneType kanbanLaneType : KanbanLaneType.values()) {
            String name = kanbanLaneType.name();
            KanbanLane kanbanLane = new DefaultKanbanLane(name, newProject, newKanban);
            kanbanLaneRepository.save(kanbanLane);
        }

        return newProject;
    }

    @Transactional
    public void update(Account account, Long projectId, ProjectUpdateCondition projectUpdateCondition) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!Objects.equals(project.getCaptain().getId(), account.getId())) {
            throw new ProjectUpdateAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        if(isSameProjectNameInAccount(projectUpdateCondition, project)) {
            throw new ProjectNameSameInAccountException(ErrorCode.PROJECT_NAME_EXISTS_IN_ACCOUNT);
        }

        project.changeProjectInform(projectUpdateCondition.getName(), projectUpdateCondition.getDescription(), projectUpdateCondition.getDeadline());
    }

    private boolean isSameProjectNameInAccount(ProjectUpdateCondition projectUpdateCondition, Project project) {
        return !Objects.equals(project.getName(), projectUpdateCondition.getName()) &&
                projectRepository.existsByName(projectUpdateCondition.getName());
    }

    public List<Project> findAllByCaptainNotTerminated(Account account) {
        return projectRepository.findAllByCaptainAndNotTerminated(account);
    }

    public List<Project> findAllByCaptainTerminated(Account account) {
        return projectRepository.findAllByCaptainAndTerminated(account);
    }

    public List<Account> findAllCrews(Account account, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if(!isCaptain(account, project) && !isCrew(account, projectId)) {
            throw new ProjectFindCrewsAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        return accountProjectRepository.findCrewsFromProject(project);
    }

    @Transactional
    public Account updateCaptain(Account account, String newCaptainUsername, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!isCaptain(account, project)) {
            throw new UpdateCaptainAccessDenied(ErrorCode.PROJECT_CAPTAIN_UPDATE_ACCESS_DENIED);
        }

        Account newCaptain = accountRepository.findByUsername(newCaptainUsername)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        accountProjectRepository.save(new AccountProject(account, project));
        accountProjectRepository.delete(project, newCaptain);
        project.changeCaptain(newCaptain);

        return newCaptain;
    }

    @Transactional
    public void deleteCrew(Account account, Long projectId, String crewNickname) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!isCaptain(account, project)) {
            throw new UpdateCaptainAccessDenied(ErrorCode.COMMON_ACCESS_DENIED);
        }

        Account crew = accountRepository.findByNickname(crewNickname)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        accountProjectRepository.delete(project, crew);
    }

    public List<Project> findAllByCrewNotTerminated(Account account) {
        return accountProjectRepository.findProjectFromCrewNotTerminated(account);
    }

    public List<Project> findAllByCrewTerminated(Account account) {
        return accountProjectRepository.findProjectFromCrewTerminated(account);
    }

    @Transactional
    public void terminateProject(Account account, Long projectId) {
        Account captain = accountRepository.findById(account.getId())
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!isCaptain(captain, project)) {
            throw new ProjectUpdateAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        project.terminateProject();
    }

    @Transactional
    public void cancelTerminateProject(Account account, Long projectId) {
        Account captain = accountRepository.findById(account.getId())
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!isCaptain(captain, project)) {
            throw new ProjectUpdateAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        project.cancelTerminateProject();
    }

    public Project findById(Long projectId, Account account) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));
    }

    @Transactional
    public void delete(Account account, Long projectId) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (!isCaptain(findAccount, project)) {
            throw new ProjectDeleteAccessDeniedException(ErrorCode.COMMON_ACCESS_DENIED);
        }

        kanbanLaneRepository.deleteByProject(project);
        kanbanRepository.deleteByProject(project);
        projectRepository.delete(project);
    }

    public boolean isCrew(Account account, Long projectId) {
        return accountProjectRepository.countByCrewInProject(account.getId(), projectId) == 1;
    }

    public boolean isCaptain(Account account, Project project) {
        return project.getCaptain().getId() == account.getId();
    }
}
