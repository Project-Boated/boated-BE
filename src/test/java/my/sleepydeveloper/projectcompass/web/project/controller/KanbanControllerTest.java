package my.sleepydeveloper.projectcompass.web.project.controller;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import my.sleepydeveloper.projectcompass.common.basetest.AcceptanceTest;
import my.sleepydeveloper.projectcompass.common.utils.AccountTestUtils;
import my.sleepydeveloper.projectcompass.common.utils.ProjectTestUtils;
import my.sleepydeveloper.projectcompass.web.project.dto.CreateKanbanLaneRequest;
import my.sleepydeveloper.projectcompass.web.project.dto.CreateProjectRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.RestAssured.given;
import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.PASSWORD;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.*;
import static my.sleepydeveloper.projectcompass.web.project.controller.document.KanbanDocument.documentKanbanLaneCreate;
import static my.sleepydeveloper.projectcompass.web.project.controller.document.ProjectDocument.documentProjectCreate;
import static org.hamcrest.Matchers.notNullValue;

@AutoConfigureMockMvc
class KanbanControllerTest extends AcceptanceTest {

    @Autowired
    MockMvc mockMvc;

    final String KANBAN_LANE_NAME = "kanbanLaneName";

    @Test
    void createKanbanLane_칸반lane만들기_정상() {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        given(this.spec)
            .filter(documentKanbanLaneCreate())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie(cookie)
            .body(new CreateKanbanLaneRequest(KANBAN_LANE_NAME))
        .when()
            .port(port)
            .post("/api/projects/{projectId}/kanban/lanes", projectId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

}