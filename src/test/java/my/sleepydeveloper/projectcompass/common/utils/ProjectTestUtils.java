package my.sleepydeveloper.projectcompass.common.utils;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.ResponseBody;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import my.sleepydeveloper.projectcompass.domain.project.service.ProjectService;
import my.sleepydeveloper.projectcompass.web.account.dto.AccountDto;
import my.sleepydeveloper.projectcompass.web.project.dto.ProjectSaveRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.restassured.RestAssured.given;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.*;
import static my.sleepydeveloper.projectcompass.web.project.controller.document.ProjectDocument.documentProjectCreate;
import static org.hamcrest.Matchers.notNullValue;

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

    public static ResponseBody createProject(int port, Cookie cookie, String projectName, String projectDescription) {
        return given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .cookie(cookie)
                    .body(new ProjectSaveRequest(projectName, projectDescription))
                .when()
                    .port(port)
                    .post("/api/projects")
                .thenReturn().getBody();
    }
}
