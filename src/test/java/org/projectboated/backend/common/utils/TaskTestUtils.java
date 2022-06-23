package org.projectboated.backend.common.utils;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.projectboated.backend.web.project.dto.CreateProjectRequest;
import org.projectboated.backend.web.task.dto.CreateTaskRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.projectboated.backend.common.data.BasicTaskData.*;
import static org.projectboated.backend.web.task.controller.document.TaskDocument.documentTaskCreate;

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

}
