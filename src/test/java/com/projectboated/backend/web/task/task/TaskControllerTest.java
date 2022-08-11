package com.projectboated.backend.web.task.task;

import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.data.BasicDataAccount;
import com.projectboated.backend.common.data.BasicDataProject;
import com.projectboated.backend.common.data.BasicDataTask;
import com.projectboated.backend.common.utils.*;
import com.projectboated.backend.infra.aws.AwsS3Service;
import com.projectboated.backend.web.task.task.document.TaskDocument;
import com.projectboated.backend.web.task.task.dto.PatchTaskRequest;
import com.projectboated.backend.web.task.task.dto.request.AssignAccountTaskRequest;
import com.projectboated.backend.web.task.task.dto.request.CreateTaskRequest;
import io.restassured.filter.Filter;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MimeType;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.List;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_ID2;
import static com.projectboated.backend.common.data.BasicDataKanbanLane.KANBAN_LANE_NAME;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static com.projectboated.backend.web.task.task.document.TaskDocument.*;
import static io.restassured.RestAssured.given;

@AutoConfigureMockMvc
class TaskControllerTest extends AcceptanceTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    public AwsS3Service awsS3Service;

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

    @Test
    void getTask_조회정상_조회성공() throws FileNotFoundException {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        int taskId = TaskTestUtils.createTask(port, cookie, projectId, TASK_NAME, TASK_DESCRIPTION, TASK_DEADLINE);
        TaskTestUtils.assignTask(port, cookie, projectId, taskId, NICKNAME);
        TaskFileTestUtils.createTaskFile(port, cookie, projectId, taskId, ResourceUtils.getFile("classpath:profile-image-test.png"), "image/jpg");

        given(this.spec)
            .filter(documentTaskRetrieve())
            .cookie(cookie)
        .when()
            .port(port)
            .get("/api/projects/{projectId}/kanban/lanes/tasks/{taskId}", projectId, taskId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deleteTask_정상request_delete성공() throws FileNotFoundException {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        int taskId = TaskTestUtils.createTask(port, cookie, projectId, TASK_NAME, TASK_DESCRIPTION, TASK_DEADLINE);

        given(this.spec)
            .filter(documentTaskDelete())
            .cookie(cookie)
        .when()
            .port(port)
            .delete("/api/projects/{projectId}/kanban/lanes/tasks/{taskId}", projectId, taskId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void patchTask_정상request_patch성공() throws FileNotFoundException {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        int taskId = TaskTestUtils.createTask(port, cookie, projectId, TASK_NAME, TASK_DESCRIPTION, TASK_DEADLINE);

        given(this.spec)
            .filter(documentTaskUpdate())
            .contentType(ContentType.JSON)
                .body(PatchTaskRequest.builder()
                        .name(TASK_NAME2)
                        .description(TASK_DESCRIPTION2)
                        .deadline(TASK_DEADLINE2)
                        .build())
            .cookie(cookie)
        .when()
            .port(port)
            .patch("/api/projects/{projectId}/kanban/lanes/tasks/{taskId}", projectId, taskId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }


    @Test
    void patchTaskLane_정상request_patch성공() throws FileNotFoundException {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        int taskId = TaskTestUtils.createTask(port, cookie, projectId, TASK_NAME, TASK_DESCRIPTION, TASK_DEADLINE);
        List<Integer> lanesId = KanbanTestUtils.getFirstKanbanLanesId(port, cookie, projectId);

        given(this.spec)
            .filter(documentTaskUpdateLane())
            .cookie(cookie)
        .when()
            .port(port)
            .put("/api/projects/{projectId}/kanban/lanes/tasks/{taskId}/lanes/{laneId}", projectId, taskId, lanesId.get(1))
        .then()
            .statusCode(HttpStatus.OK.value());
    }




}