package com.projectboated.backend.web.kanban.controller;

import com.projectboated.backend.common.data.BasicAccountData;
import com.projectboated.backend.common.data.BasicKanbanData;
import com.projectboated.backend.common.data.BasicProjectData;
import com.projectboated.backend.common.data.BasicTaskData;
import com.projectboated.backend.common.utils.KanbanTestUtils;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import com.projectboated.backend.common.utils.TaskTestUtils;
import com.projectboated.backend.web.kanban.dto.CreateKanbanLaneRequest;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.utils.AccountTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.RestAssured.given;
import static com.projectboated.backend.web.project.controller.document.KanbanDocument.*;

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
            .body(new CreateKanbanLaneRequest(BasicKanbanData.KANBAN_LANE_NAME))
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

        KanbanTestUtils.createCustomKanbanLane(port, cookie, projectId, BasicKanbanData.KANBAN_LANE_NAME);

        given(this.spec)
            .filter(documentKanbanLaneDelete())
            .cookie(cookie)
        .when()
            .port(port)
            .delete("/api/projects/{projectId}/kanban/lanes", projectId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getKanban_칸반lane조회_정상() {
        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, BasicProjectData.PROJECT_DEADLINE);

        KanbanTestUtils.createCustomKanbanLane(port, cookie, projectId, BasicKanbanData.KANBAN_LANE_NAME);
        TaskTestUtils.createTask(port, cookie, projectId, BasicTaskData.TASK_NAME, BasicTaskData.TASK_DESCRIPTION, BasicTaskData.TASK_DEADLINE);

        given(this.spec)
            .filter(documentKanbanRetrieve())
            .cookie(cookie)
        .when()
            .port(port)
            .get("/api/projects/{projectId}/kanban", projectId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

}