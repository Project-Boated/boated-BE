package com.projectboated.backend.common.utils;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.projectboated.backend.web.kanban.kanbanlane.dto.request.CreateKanbanLaneRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static int getFirstKanbanLaneId(int port, Cookie cookie, int projectId) {
        return given()
            .cookie(cookie)
            .accept(ContentType.JSON)
        .when()
            .port(port)
            .get("/api/projects/{projectId}/kanban", projectId)
        .thenReturn()
                .jsonPath().getInt("lanes[0].id");
    }

    public static List<Integer> getFirstKanbanLanesId(int port, Cookie cookie, int projectId) {
        List<Map<String, Object>> mapList = given()
            .cookie(cookie)
            .accept(ContentType.JSON)
        .when()
            .port(port)
            .get("/api/projects/{projectId}/kanban", projectId)
        .thenReturn()
                .jsonPath().getList("lanes");

        List<Integer> result = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            result.add((Integer) map.get("id"));
        }
        return result;
    }
}
