package org.projectboated.backend.web.kanban.controller;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import org.projectboated.backend.common.basetest.AcceptanceTest;
import org.projectboated.backend.common.data.BasicAccountData;
import org.projectboated.backend.common.data.BasicProjectData;
import org.projectboated.backend.common.utils.AccountTestUtils;
import org.projectboated.backend.common.utils.KanbanTestUtils;
import org.projectboated.backend.common.utils.ProjectTestUtils;
import org.projectboated.backend.web.kanban.dto.CreateKanbanLaneRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.RestAssured.given;
import static org.projectboated.backend.common.data.BasicKanbanData.KANBAN_LANE_NAME;
import static org.projectboated.backend.web.project.controller.document.KanbanDocument.documentKanbanLaneCreate;
import static org.projectboated.backend.web.project.controller.document.KanbanDocument.documentKanbanLaneDelete;

@AutoConfigureMockMvc
class KanbanControllerTest extends AcceptanceTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void createCustomKanbanLane_칸반lane만들기_정상() {
        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, BasicProjectData.PROJECT_DEADLINE);

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

    @Test
    void deleteCustomKanbanLane_칸반lane지우기_정상() {
        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, BasicProjectData.PROJECT_DEADLINE);

        KanbanTestUtils.createCustomKanbanLane(port, cookie, projectId, KANBAN_LANE_NAME);

        given(this.spec)
            .filter(documentKanbanLaneDelete())
            .cookie(cookie)
        .when()
            .port(port)
            .delete("/api/projects/{projectId}/kanban/lanes", projectId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

}