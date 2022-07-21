package com.projectboated.backend.web.kanban.document;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KanbanLaneDocument {

    public static RestDocumentationFilter documentTaskOrderChange() {
        return document("kanban-lanes-tasks-order-change",
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 고유 번호"),
                        parameterWithName("originalLaneIndex").description("바꾸고 싶은 task의 lane index(index번호입니다. id아닙니다)"),
                        parameterWithName("originalTaskIndex").description("바꾸고 싶은 task의 index(index번호입니다. id아닙니다)"),
                        parameterWithName("changeLaneIndex").description("바꿔지게 될 task의 lane index(index번호입니다. id아닙니다)"),
                        parameterWithName("changeTaskIndex").description("바꿔지게 될 task의 index(index번호입니다. id아닙니다)")
                )
        );
    }

}
