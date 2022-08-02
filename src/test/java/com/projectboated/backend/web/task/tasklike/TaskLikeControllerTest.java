package com.projectboated.backend.web.task.tasklike;

import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.utils.AccountTestUtils;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import com.projectboated.backend.common.utils.TaskLikeTestUtils;
import com.projectboated.backend.common.utils.TaskTestUtils;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.stream.IntStream;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static com.projectboated.backend.web.task.tasklike.document.TaskLikeController.documentCancelTaskLike;
import static com.projectboated.backend.web.task.tasklike.document.TaskLikeController.documentTaskLike;
import static io.restassured.RestAssured.given;

class TaskLikeControllerTest extends AcceptanceTest {

    @Test
    void likeTask_하나찜하기_정상() {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        int[] taskIds = IntStream.range(0, 5)
                .map((i) -> TaskTestUtils.createTask(port, cookie, projectId, TASK_NAME + i, TASK_DESCRIPTION, TASK_DEADLINE))
                .toArray();

        given(this.spec)
            .filter(documentTaskLike())
            .cookie(cookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/tasks/{taskId}/like", projectId, taskIds[0])
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void cancelTaskLike_찜취소_정상() {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        int[] taskIds = IntStream.range(0, 5)
                .map((i) -> TaskTestUtils.createTask(port, cookie, projectId, TASK_NAME + i, TASK_DESCRIPTION, TASK_DEADLINE))
                .toArray();
        TaskLikeTestUtils.createTaskLike(port, cookie, projectId, taskIds[0]);

        given(this.spec)
            .filter(documentCancelTaskLike())
            .cookie(cookie)
        .when()
            .port(port)
            .delete("/api/projects/{projectId}/tasks/{taskId}/like", projectId, taskIds[0])
        .then()
            .statusCode(HttpStatus.OK.value());
    }

}