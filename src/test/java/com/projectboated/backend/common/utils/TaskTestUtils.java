package com.projectboated.backend.common.utils;

import com.projectboated.backend.web.task.task.dto.request.AssignAccountTaskRequest;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.projectboated.backend.web.task.task.dto.request.CreateTaskRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;

@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskTestUtils {

    public static int createTask(int port, Cookie cookie, int projectId, String name, String description, LocalDateTime deadline) {
        return given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie(cookie)
            .body(new CreateTaskRequest(name, description, deadline))
        .when()
            .port(port)
            .post("/api/projects/{projectId}/kanban/lanes/tasks", projectId)
        .thenReturn()
            .body().jsonPath().getInt("id");
    }

    public static void assignTask(int port, Cookie cookie, int projectId, int taskId, String nickname) {
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie(cookie)
            .body(new AssignAccountTaskRequest(nickname))
        .when()
            .port(port)
            .post("/api/projects/{projectId}/kanban/lanes/tasks/{taskId}/assign", projectId, taskId);
    }

}
