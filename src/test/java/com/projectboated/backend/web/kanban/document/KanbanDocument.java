package com.projectboated.backend.web.kanban.document;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KanbanDocument {

    public static RestDocumentationFilter documentKanbanLaneCreate() {
        return document("kanban-lane-create",
                preprocessResponse(prettyPrint()),
                requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 고유 번호")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("Kanban lane 이름")
                )
        );
    }

    public static RestDocumentationFilter documentKanbanLaneDelete() {
        return document("kanban-lane-delete",
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 고유 번호"),
                        parameterWithName("kanbanLaneId").description("kanban lane 고유번호")

                )
        );
    }

    public static RestDocumentationFilter documentKanbanRetrieve() {
        return document("kanban-retrieve",
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 고유번호")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("lanes").type(JsonFieldType.ARRAY).description("Kanban의 lane들"),
                        fieldWithPath("lanes[].id").type(JsonFieldType.NUMBER).description("lane 고유번호"),
                        fieldWithPath("lanes[].name").type(JsonFieldType.STRING).description("lane 이름"),
                        fieldWithPath("lanes[].tasks").type(JsonFieldType.ARRAY).description("lane에 있는 task"),
                        fieldWithPath("lanes[].tasks[].id").type(JsonFieldType.NUMBER).description("task 고유번호"),
                        fieldWithPath("lanes[].tasks[].name").type(JsonFieldType.STRING).description("task 이름"),
                        fieldWithPath("lanes[].tasks[].description").type(JsonFieldType.STRING).description("task 설명"),
                        fieldWithPath("lanes[].tasks[].deadline").type(JsonFieldType.STRING).description("task 마감기한"),
                        fieldWithPath("lanes[].tasks[].dday").type(JsonFieldType.NUMBER).description("task DDay"),
                        fieldWithPath("lanes[].tasks[].fileCount").type(JsonFieldType.NUMBER).description("task가 가진 파일 개수"),
                        fieldWithPath("lanes[].tasks[].assignedAccounts").type(JsonFieldType.ARRAY).description("task에 배정된 Account"),
                        fieldWithPath("lanes[].tasks[].assignedAccounts[].id").type(JsonFieldType.NUMBER).description("task에 배정된 Account 고유번호"),
                        fieldWithPath("lanes[].tasks[].assignedAccounts[].nickname").type(JsonFieldType.STRING).description("task에 배정된 Account nickname")
                )
        );
    }

    public static RestDocumentationFilter documentKanbanLaneOrderChange() {
        return document("kanban-lanes-order-change",
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 고유 번호"),
                        parameterWithName("originalIndex").description("바꾸고 싶은 index(index번호입니다. id아닙니다)"),
                        parameterWithName("changeIndex").description("바꿔지게 될 index(index번호입니다. id아닙니다)")
                )
        );
    }

}
