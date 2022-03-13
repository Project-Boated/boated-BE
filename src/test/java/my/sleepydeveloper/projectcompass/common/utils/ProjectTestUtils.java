package my.sleepydeveloper.projectcompass.common.utils;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.*;

@Service
@Transactional
public class ProjectTestUtils {

    private final ProjectService projectService;

    public ProjectTestUtils(ProjectService projectService) {
        this.projectService = projectService;
    }

    public Project createProject(Account account) {
        return projectService.save(new Project(projectName, projectDescription, account));
    }

    public Project createProject(Account account, String projectName, String projectDescription) {
        return projectService.save(new Project(projectName, projectDescription, account));
    }
}
