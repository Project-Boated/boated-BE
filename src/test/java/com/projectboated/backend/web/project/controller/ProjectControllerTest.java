package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.web.project.dto.request.CreateProjectRequest;
import com.projectboated.backend.web.project.dto.request.PatchProjectRequest;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.utils.AccountTestUtils;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import com.projectboated.backend.domain.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static com.projectboated.backend.web.project.controller.document.ProjectDocument.*;

@AutoConfigureMockMvc
class ProjectControllerTest extends AcceptanceTest {

    @Autowired
    ProjectTestUtils projectTestUtils;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProjectService projectService;

    @Test
    void createProject_프로젝트생성_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        // When
        // Then
        given(this.spec)
                .filter(documentProjectCreate())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .cookie(cookie)
                .body(new CreateProjectRequest(PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE))
        .when()
                .port(port)
                .post("/api/projects")
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("id", notNullValue());
    }


    @Test
    void patchProject_모든필드업데이트_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        String projectUpdateName = "updatedProjectName";
        String projectUpdateDescription = "updateDescription";

        // When
        // Then
        given(this.spec)
                .filter(documentProjectUpdate())
                .contentType(ContentType.JSON)
                .cookie(cookie)
                .body(new PatchProjectRequest(projectUpdateName, projectUpdateDescription, LocalDateTime.now().plus(1, ChronoUnit.DAYS)))
        .when()
                .port(port)
                .patch("/api/projects/" + projectId)
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("id", notNullValue());
    }

    @Test
    void deleteProject_프로젝트삭제_정상() {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        given(this.spec)
            .filter(documentProjectDelete())
            .contentType(ContentType.JSON)
            .cookie(cookie)
        .when()
            .port(port)
            .delete("/api/projects/{projectId}", projectId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void terminateProject_프로젝트를종료시킴_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie captainCookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        int projectId = ProjectTestUtils.createProject(port, captainCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectTerminate())
            .accept(ContentType.JSON)
            .cookie(captainCookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/terminate", projectId)
        .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("id", equalTo(projectId));
    }

    @Test
    void cancelTerminateProject_종료된프로젝트를시작_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie captainCookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        int projectId = ProjectTestUtils.createProject(port, captainCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        ProjectTestUtils.terminateProject(port, captainCookie, projectId);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectCancelTerminate())
            .accept(ContentType.JSON)
            .cookie(captainCookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/cancel-terminate", projectId)
        .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("id", equalTo(projectId));
    }

    @Test
    void getProject_프로젝트프로필조회_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectRetrieve())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie(cookie)
        .when()
            .port(port)
            .get("/api/projects/{projectId}", projectId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }
}