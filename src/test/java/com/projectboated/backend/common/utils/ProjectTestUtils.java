package com.projectboated.backend.common.utils;

import com.projectboated.backend.web.project.dto.request.CreateProjectRequest;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public abstract class ProjectTestUtils {

    public static int createProject(int port, Cookie cookie, String projectName, String projectDescription, LocalDateTime deadline) {
        return given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .cookie(cookie)
                    .body(new CreateProjectRequest(projectName, projectDescription, deadline))
                .when()
                    .port(port)
                    .post("/api/projects")
                .thenReturn()
                    .body().jsonPath().getInt("id");
    }

    public static void terminateProject(int port, Cookie captainCookie, long projectId) {
        given()
            .accept(ContentType.JSON)
            .cookie(captainCookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/terminate", projectId)
        .then().extract().body();
    }
}
