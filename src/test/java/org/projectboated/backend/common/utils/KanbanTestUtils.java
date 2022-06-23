package org.projectboated.backend.common.utils;

import io.restassured.http.Cookie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.projectboated.backend.web.kanban.dto.CreateKanbanLaneRequest;

import static io.restassured.RestAssured.given;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KanbanTestUtils {

    public static void createCustomKanbanLane(int port, Cookie captainCookie, long projectId, String kanbanLaneName) {
        given()
            .cookie(captainCookie)
            .body(new CreateKanbanLaneRequest(kanbanLaneName))
        .when()
            .port(port)
            .post("/api/projects/{projectId}/kanban/lanes", projectId);
    }

}
