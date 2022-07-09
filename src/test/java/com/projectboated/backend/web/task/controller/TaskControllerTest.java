package com.projectboated.backend.web.task.controller;

import com.projectboated.backend.common.data.BasicDataAccount;
import com.projectboated.backend.common.data.BasicDataProject;
import com.projectboated.backend.common.data.BasicDataTask;
import com.projectboated.backend.common.utils.KanbanTestUtils;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import com.projectboated.backend.common.utils.TaskTestUtils;
import com.projectboated.backend.web.task.dto.request.AssignAccountTaskRequest;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.utils.AccountTestUtils;
import com.projectboated.backend.web.task.dto.request.CreateTaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataKanban.KANBAN_LANE_NAME;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static com.projectboated.backend.web.task.controller.document.TaskDocument.*;
import static io.restassured.RestAssured.given;

@AutoConfigureMockMvc
class TaskControllerTest extends AcceptanceTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void createTask_task하나생성_정상() {
        AccountTestUtils.createAccount(port, BasicDataAccount.USERNAME, BasicDataAccount.PASSWORD, BasicDataAccount.NICKNAME, BasicDataAccount.PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, BasicDataAccount.USERNAME, BasicDataAccount.PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, BasicDataProject.PROJECT_NAME, BasicDataProject.PROJECT_DESCRIPTION, BasicDataProject.PROJECT_DEADLINE);

        given(this.spec)
            .filter(documentTaskCreate())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie(cookie)
            .body(new CreateTaskRequest(BasicDataTask.TASK_NAME, BasicDataTask.TASK_DESCRIPTION, BasicDataTask.TASK_DEADLINE))
        .when()
            .port(port)
            .post("/api/projects/{projectId}/kanban/lanes/tasks", projectId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void assignAccountTask_account하나assign_정상() {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        KanbanTestUtils.createCustomKanbanLane(port, cookie, projectId, KANBAN_LANE_NAME);
        int taskId = TaskTestUtils.createTask(port, cookie, projectId, TASK_NAME, TASK_DESCRIPTION, TASK_DEADLINE);

        given(this.spec)
            .filter(documentTaskAssign())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie(cookie)
            .body(new AssignAccountTaskRequest(NICKNAME))
        .when()
            .port(port)
            .post("/api/projects/{projectId}/kanban/lanes/tasks/{taskId}/assign", projectId, taskId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void cancelAssignAccountTask_assign취소_정상() {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        KanbanTestUtils.createCustomKanbanLane(port, cookie, projectId, KANBAN_LANE_NAME);
        int taskId = TaskTestUtils.createTask(port, cookie, projectId, TASK_NAME, TASK_DESCRIPTION, TASK_DEADLINE);
        TaskTestUtils.assignTask(port, cookie, projectId, taskId, NICKNAME);

        given(this.spec)
            .filter(documentTaskCancelAssign())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie(cookie)
            .body(new AssignAccountTaskRequest(NICKNAME))
        .when()
            .port(port)
            .post("/api/projects/{projectId}/kanban/lanes/tasks/{taskId}/cancel-assign", projectId, taskId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

}