package com.projectboated.backend.web.kanban.document;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KanbanLaneDocument {

    public static RestDocumentationFilter documentKanbanLaneIndexChange() {
        return document("kanban-lane-index-change",
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 고유 번호"),
                        parameterWithName("originalIndex").description("바꾸고 싶은 index(index번호입니다. id아닙니다)"),
                        parameterWithName("changeIndex").description("바꾸고 싶은 index(index번호입니다. id아닙니다)")
                )
        );
    }

}
