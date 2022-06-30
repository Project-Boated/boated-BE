package com.projectboated.backend.web.task.controller;

import com.projectboated.backend.common.data.BasicAccountData;
import com.projectboated.backend.common.data.BasicProjectData;
import com.projectboated.backend.common.data.BasicTaskData;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.utils.AccountTestUtils;
import com.projectboated.backend.web.task.dto.CreateTaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.RestAssured.given;
import static com.projectboated.backend.web.task.controller.document.TaskDocument.documentTaskCreate;

@AutoConfigureMockMvc
class TaskControllerTest extends AcceptanceTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void createTask_task하나생성_정상() {
        AccountTestUtils.createAccount(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicAccountData.USERNAME, BasicAccountData.PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, BasicProjectData.PROJECT_NAME, BasicProjectData.PROJECT_DESCRIPTION, BasicProjectData.PROJECT_DEADLINE);

        given(this.spec)
            .filter(documentTaskCreate())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie(cookie)
            .body(new CreateTaskRequest(BasicTaskData.TASK_NAME, BasicTaskData.TASK_DESCRIPTION, BasicTaskData.TASK_DEADLINE))
        .when()
            .port(port)
            .post("/api/projects/{projectId}/kanban/lanes/tasks", projectId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

}