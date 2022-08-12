package com.projectboated.backend.web.kanban.controller;

import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.utils.AccountTestUtils;
import com.projectboated.backend.common.utils.KanbanTestUtils;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import com.projectboated.backend.common.utils.TaskTestUtils;
import com.projectboated.backend.web.kanban.kanbanlane.dto.UpdateKanbanLaneRequest;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;

import java.util.stream.IntStream;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static com.projectboated.backend.web.kanban.document.KanbanLaneDocument.*;
import static io.restassured.RestAssured.given;

@AutoConfigureMockMvc
class KanbanLaneControllerTest extends AcceptanceTest {

    @Test
    void changeTaskOrder_task옮기기_정상() {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        IntStream.range(0, 5)
                        .forEach((i) -> TaskTestUtils.createTask(port, cookie, projectId, TASK_NAME+i, TASK_DESCRIPTION, TASK_DEADLINE));
        int kanbanLaneId = KanbanTestUtils.getFirstKanbanLanesId(port, cookie, projectId).get(0);

        // When
        // Then
        given(this.spec)
            .filter(documentTaskOrderChange())
            .cookie(cookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/kanban/lanes/tasks/change/{originalLaneId}/{originalTaskIndex}/{changeLaneId}/{changeTaskIndex}", projectId, kanbanLaneId, 0, kanbanLaneId, 0)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void updateKanbanLane_kanbanLane업데이트_정상() {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        int kanbanLaneId = KanbanTestUtils.getFirstKanbanLaneId(port, cookie, projectId);

        // When
        // Then
        given(this.spec)
            .filter(documentUpdateKanbanLane())
            .contentType(ContentType.JSON)
            .body(new UpdateKanbanLaneRequest("newName"))
            .cookie(cookie)
        .when()
            .port(port)
            .put("/api/projects/{projectId}/kanban/lanes/{kanbanLaneId}", projectId, kanbanLaneId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getLanes_lanes조회_조회정상() {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        // When
        // Then
        given(this.spec)
            .filter(documentLanesRetrieve())
            .cookie(cookie)
        .when()
            .port(port)
            .get("/api/projects/{projectId}/kanban/lanes", projectId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }



}