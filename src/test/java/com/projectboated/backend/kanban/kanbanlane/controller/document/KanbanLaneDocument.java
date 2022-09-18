package com.projectboated.backend.kanban.kanbanlane.controller.document;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KanbanLaneDocument {

    public static RestDocumentationResultHandler documentUpdateKanbanLane() {
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

    public static RestDocumentationResultHandler documentLanesRetrieve() {
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

    public static RestDocumentationResultHandler documentKanbanLaneOrderChange() {
        return document("kanban-lanes-order-change",
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 고유 번호"),
                        parameterWithName("originalIndex").description("바꾸고 싶은 index(index번호입니다. id아닙니다)"),
                        parameterWithName("changeIndex").description("바꿔지게 될 index(index번호입니다. id아닙니다)")
                )
        );
    }

}
