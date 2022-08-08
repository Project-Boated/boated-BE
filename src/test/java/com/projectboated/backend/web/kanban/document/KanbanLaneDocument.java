package com.projectboated.backend.web.kanban.document;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KanbanLaneDocument {

    public static RestDocumentationFilter documentTaskOrderChange() {
        return document("kanban-lanes-tasks-order-change",
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 고유 번호"),
                        parameterWithName("originalLaneId").description("바꾸고 싶은 lane의 고유번호"),
                        parameterWithName("originalTaskIndex").description("바꾸고 싶은 task의 index(index번호입니다. id아닙니다)"),
                        parameterWithName("changeLaneId").description("바꿔지게 될 lane의 고유번호"),
                        parameterWithName("changeTaskIndex").description("바꿔지게 될 task의 index(index번호입니다. id아닙니다)")
                )
        );
    }

    public static RestDocumentationFilter documentUpdateKanbanLane() {
        return document("kanban-lanes-update",
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 고유 번호"),
                        parameterWithName("kanbanLaneId").description("업데이트할 lane의 고유번호")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("바꾸고 싶은 name").optional()
                )
        );
    }

    public static RestDocumentationFilter documentLanesRetrieve() {
        return document("kanban-lanes-retrieve",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 고유 번호")
                ),
                responseFields(
                        fieldWithPath("lanes[]").type(JsonFieldType.ARRAY).description("lane 정보"),
                        fieldWithPath("lanes[].id").type(JsonFieldType.NUMBER).description("lane의 고유번호"),
                        fieldWithPath("lanes[].name").type(JsonFieldType.STRING).description("lane의 이름")
                )
        );
    }

}
