package com.projectboated.backend.web.kanban.controller;

import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.utils.AccountTestUtils;
import com.projectboated.backend.common.utils.KanbanTestUtils;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import com.projectboated.backend.common.utils.TaskTestUtils;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;

import java.util.stream.IntStream;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static com.projectboated.backend.web.kanban.document.KanbanLaneDocument.documentTaskOrderChange;
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
        int firstKanbanId = KanbanTestUtils.getFirstKanbanLaneId(port, cookie, projectId);

        // When
        // Then
        given(this.spec)
            .filter(documentTaskOrderChange())
            .cookie(cookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/kanban/lanes/{laneId}/tasks/change/{originalIndex}/{changeIndex}", projectId, firstKanbanId, 1, 2)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

}