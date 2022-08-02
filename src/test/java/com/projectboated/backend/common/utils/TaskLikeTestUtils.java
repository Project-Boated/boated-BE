package com.projectboated.backend.common.utils;

import io.restassured.http.Cookie;
import org.springframework.http.HttpStatus;

import static com.projectboated.backend.web.task.tasklike.document.TaskLikeController.documentTaskLike;
import static io.restassured.RestAssured.given;

public abstract class TaskLikeTestUtils {

    public static void createTaskLike(int port, Cookie cookie, int projectId, int taskId) {
        given()
            .cookie(cookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/tasks/{taskId}/like", projectId, taskId);
    }

}
