package com.projectboated.backend.common.utils;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;

import java.io.File;

import static io.restassured.RestAssured.given;

public abstract class TaskFileTestUtils {

    public static int createTaskFile(int port, Cookie cookie, int projectId, int taskId, File file, String mimeType) {
        return given()
            .contentType(ContentType.MULTIPART)
            .multiPart("file", file, mimeType)
            .cookie(cookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/tasks/{taskId}/files", projectId, taskId)
        .thenReturn().jsonPath().get("id");
    }

}
