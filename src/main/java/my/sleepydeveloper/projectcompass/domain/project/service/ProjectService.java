package my.sleepydeveloper.projectcompass.domain.project.service;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.accountproject.entity.AccountProject;
import my.sleepydeveloper.projectcompass.domain.account.exception.AccountNotFoundException;
import my.sleepydeveloper.projectcompass.domain.accountproject.repository.AccountProjectRepository;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectNotFoundException;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectUpdateAccessDeniedException;
import my.sleepydeveloper.projectcompass.domain.project.exception.ProjectNameSameInAccountException;
import my.sleepydeveloper.projectcompass.domain.project.exception.UpdateCaptainAccessDenied;
import my.sleepydeveloper.projectcompass.domain.project.repository.ProjectRepository;
import my.sleepydeveloper.projectcompass.domain.project.vo.ProjectUpdateCondition;
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
    private final AccountProjectRepository accountProjectRepository;

    @Transactional
    public Project save(Project project) {
        if (projectRepository.existsByNameAndCaptain(project.getName(), project.getCaptain())) {
            throw new ProjectNameSameInAccountException(ErrorCode.PROJECT_NAME_EXISTS_IN_ACCOUNT);
        }

        return projectRepository.save(project);
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

        project.changeProjectInform(projectUpdateCondition.getName(), projectUpdateCondition.getDescription());
    }

    private boolean isSameProjectNameInAccount(ProjectUpdateCondition projectUpdateCondition, Project project) {
        return !Objects.equals(project.getName(), projectUpdateCondition.getName()) &&
                projectRepository.existsByName(projectUpdateCondition.getName());
    }

    public List<Project> findAllByCaptain(Account account) {
        return projectRepository.findAllByCaptain(account);
    }

    public List<Account> findAllCrews(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        return accountProjectRepository.findCrewsFromProject(project);
    }

    @Transactional
    public Account updateCaptain(Account account, String newCaptainUsername, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        if (project.getCaptain().getId() != account.getId()) {
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

        if (project.getCaptain().getId() != account.getId()) {
            throw new UpdateCaptainAccessDenied(ErrorCode.COMMON_ACCESS_DENIED);
        }

        Account crew = accountRepository.findByNickname(crewNickname)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND));

        accountProjectRepository.delete(project, crew);
    }

    public List<Project> findAllByCrew(Account account) {
        accountProjectRepository.findProjectFromCrew(account);
        return null;
    }
}
