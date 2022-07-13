package com.projectboated.backend.web.kanban.controller;

import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.utils.AccountTestUtils;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static com.projectboated.backend.web.kanban.document.KanbanLaneDocument.documentKanbanLaneIndexChange;
import static io.restassured.RestAssured.given;

class KanbanLaneControllerTest extends AcceptanceTest {

    @Test
    void changeKanbanLaneIndex_칸반lane옮기기_정상() {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        given(this.spec)
            .filter(documentKanbanLaneIndexChange())
            .cookie(cookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/kanban/lanes/change/{originalIndex}/{changeIndex}", projectId, 1, 2)
        .then()
            .statusCode(HttpStatus.OK.value());
    }



}