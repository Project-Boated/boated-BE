package com.projectboated.backend.common.utils;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.projectboated.backend.web.kanban.kanbanlane.dto.request.CreateKanbanLaneRequest;

import static io.restassured.RestAssured.given;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KanbanTestUtils {

    public static void createCustomKanbanLane(int port, Cookie captainCookie, long projectId, String kanbanLaneName) {
        given()
            .cookie(captainCookie)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(new CreateKanbanLaneRequest(kanbanLaneName))
        .when()
            .port(port)
            .post("/api/projects/{projectId}/kanban/lanes", projectId);
    }

}
