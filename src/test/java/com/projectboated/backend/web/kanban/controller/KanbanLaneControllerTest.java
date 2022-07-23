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
import org.springframework.http.MediaType;

import java.util.stream.IntStream;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static com.projectboated.backend.web.kanban.document.KanbanLaneDocument.documentTaskOrderChange;
import static com.projectboated.backend.web.kanban.document.KanbanLaneDocument.documentUpdateKanbanLane;
import static io.restassured.RestAssured.given;

@AutoConfigureMockMvc
class KanbanLaneControllerTest extends AcceptanceTest {

    @Test
    void changeTaskOrder_task옮기기_정상() {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        IntStream.range(0, 5)
                        .forEach((i) -> TaskTestUtils.createTask(port, cookie, projectId, TASK_NAME+i, TASK_DESCRIPTION, TASK_DEADLINE));

        // When
        // Then
        given(this.spec)
            .filter(documentTaskOrderChange())
            .cookie(cookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/kanban/lanes/tasks/change/{originalLaneIndex}/{originalTaskIndex}/{changeLaneIndex}/{changeTaskIndex}", projectId, 0, 0, 1, 0)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void updateKanbanLane_kanbanLane업데이트_정상() {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        // When
        // Then
        given(this.spec)
            .filter(documentUpdateKanbanLane())
            .contentType(ContentType.JSON)
            .body(new UpdateKanbanLaneRequest("newName"))
            .cookie(cookie)
        .when()
            .port(port)
            .put("/api/projects/{projectId}/kanban/lanes/{kanbanLaneIndex}", projectId, 0)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

}