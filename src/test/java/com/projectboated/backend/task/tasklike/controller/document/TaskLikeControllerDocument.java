package com.projectboated.backend.task.tasklike.controller.document;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

public abstract class TaskLikeControllerDocument {

    public static RestDocumentationResultHandler documentMyTaskLikeRetrieve() {
        return document("tasks-like-my-retrieve",
                preprocessResponse(prettyPrint()),
                responseFields(
                        fieldWithPath("taskLikes").type(JsonFieldType.ARRAY).description("contents"),
                        fieldWithPath("taskLikes[].project").type(JsonFieldType.OBJECT).description("project"),
                        fieldWithPath("taskLikes[].project.id").type(JsonFieldType.NUMBER).description("project의 고유번호"),
                        fieldWithPath("taskLikes[].project.name").type(JsonFieldType.STRING).description("project의 이름"),
                        fieldWithPath("taskLikes[].project.description").type(JsonFieldType.STRING).description("project의 설명"),
                        fieldWithPath("taskLikes[].project.deadline").type(JsonFieldType.STRING).description("project의 deadline"),
                        fieldWithPath("taskLikes[].project.dday").type(JsonFieldType.NUMBER).description("project의 D-day"),
                        fieldWithPath("taskLikes[].project.totalDay").type(JsonFieldType.NUMBER).description("project의 총기간"),
                        fieldWithPath("taskLikes[].project.terminated").type(JsonFieldType.BOOLEAN).description("project가 종료되었는가?"),
                        fieldWithPath("taskLikes[].task").type(JsonFieldType.OBJECT).description("task"),
                        fieldWithPath("taskLikes[].task.id").type(JsonFieldType.NUMBER).description("task의 고유번호"),
                        fieldWithPath("taskLikes[].task.name").type(JsonFieldType.STRING).description("task의 이름"),
                        fieldWithPath("taskLikes[].task.description").type(JsonFieldType.STRING).description("task의 설명"),
                        fieldWithPath("taskLikes[].task.deadline").type(JsonFieldType.STRING).description("task의 deadline"),
                        fieldWithPath("taskLikes[].task.dday").type(JsonFieldType.NUMBER).description("task의 dday"),
                        fieldWithPath("taskLikes[].task.fileCount").type(JsonFieldType.NUMBER).description("task의 파일의 총 수"),
                        fieldWithPath("taskLikes[].task.assignedAccounts").type(JsonFieldType.ARRAY).description("task에 배정된 account"),
                        fieldWithPath("taskLikes[].task.assignedAccounts[].id").type(JsonFieldType.NUMBER).description("account의 고유번호"),
                        fieldWithPath("taskLikes[].task.assignedAccounts[].nickname").type(JsonFieldType.STRING).description("account의 이름")
                )
        );
    }

    public static RestDocumentationResultHandler documentTaskLikeOrderChange() {
        return document("tasks-like-my-order-change",
                pathParameters(
                        parameterWithName("originalIndex").description("원래 index"),
                        parameterWithName("changeIndex").description("바꿀 index")
                )
        );
    }

    public static RestDocumentationResultHandler documentTaskLike() {
        return document("tasks-like",
                pathParameters(
                        parameterWithName("projectId").description("찜할 프로젝트의 id"),
                        parameterWithName("taskId").description("찜할 task의 id")
                )
        );
    }

    public static RestDocumentationResultHandler documentCancelTaskLike() {
        return document("tasks-like-cancel",
                pathParameters(
                        parameterWithName("projectId").description("찜을 취소할 프로젝트의 id"),
                        parameterWithName("taskId").description("찜을 취소할 task의 id")
                )
        );
    }

}
