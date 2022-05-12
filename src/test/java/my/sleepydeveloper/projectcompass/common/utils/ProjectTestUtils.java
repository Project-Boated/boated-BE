package my.sleepydeveloper.projectcompass.common.utils;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import my.sleepydeveloper.projectcompass.domain.project.service.ProjectService;
import my.sleepydeveloper.projectcompass.web.project.dto.CreateProjectRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static my.sleepydeveloper.projectcompass.web.project.controller.document.ProjectDocument.documentProjectTerminate;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Service
@Transactional
public class ProjectTestUtils {

    private final ProjectService projectService;

    public ProjectTestUtils(ProjectService projectService) {
        this.projectService = projectService;
    }

    public static int createProject(int port, Cookie cookie, String projectName, String projectDescription, LocalDateTime deadline) {
        return given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .cookie(cookie)
                    .body(new CreateProjectRequest(projectName, projectDescription, deadline))
                .when()
                    .port(port)
                    .post("/api/projects")
                .thenReturn()
                    .body().jsonPath().getInt("id");
    }

    public static void terminateProject(int port, Cookie captainCookie, long projectId) {
        given()
            .accept(ContentType.JSON)
            .cookie(captainCookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/terminate", projectId)
        .then().extract().body();
    }
}
