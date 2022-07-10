package com.projectboated.backend.web.kanban.controller;

import com.projectboated.backend.common.utils.KanbanTestUtils;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import com.projectboated.backend.common.utils.TaskTestUtils;
import com.projectboated.backend.web.kanban.kanbanlane.dto.request.CreateKanbanLaneRequest;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.utils.AccountTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataKanban.*;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static io.restassured.RestAssured.given;
import static com.projectboated.backend.web.project.controller.document.KanbanDocument.*;

@AutoConfigureMockMvc
class KanbanControllerTest extends AcceptanceTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void createCustomKanbanLane_칸반lane만들기_정상() {
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

    @Test
    void deleteCustomKanbanLane_칸반lane지우기_정상() {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

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

    @Test
    void getKanban_칸반lane조회_정상() {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        KanbanTestUtils.createCustomKanbanLane(port, cookie, projectId, KANBAN_LANE_NAME);
        int taskId = TaskTestUtils.createTask(port, cookie, projectId, TASK_NAME, TASK_DESCRIPTION, TASK_DEADLINE);
        TaskTestUtils.assignTask(port, cookie, projectId, taskId, NICKNAME);

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