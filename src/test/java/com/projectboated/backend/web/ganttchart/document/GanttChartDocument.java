package com.projectboated.backend.web.ganttchart.document;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

public abstract class GanttChartDocument {

    public static RestDocumentationResultHandler documentMyGanttChartRetrieve() {
        return document("gantt-chart-my-retrieve",
                preprocessResponse(prettyPrint()),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("projects").type(JsonFieldType.ARRAY).description("프로젝트 목록"),
                        fieldWithPath("projects[].id").type(JsonFieldType.NUMBER).description("프로젝트 고유번호"),
                        fieldWithPath("projects[].name").type(JsonFieldType.STRING).description("프로젝트 이름"),
                        fieldWithPath("projects[].createdDate").type(JsonFieldType.STRING).description("프로젝트 만든날짜"),
                        fieldWithPath("projects[].deadline").type(JsonFieldType.STRING).description("프로젝트 deadline"),
                        fieldWithPath("projects[].assignedTasks").type(JsonFieldType.ARRAY).description("배정된 task목록"),
                        fieldWithPath("projects[].assignedTasks[].id").type(JsonFieldType.NUMBER).description("배정된 task의 고유번호"),
                        fieldWithPath("projects[].assignedTasks[].name").type(JsonFieldType.STRING).description("배정된 task의 이름"),
                        fieldWithPath("projects[].assignedTasks[].createdDate").type(JsonFieldType.STRING).description("배정된 task의 만든날짜"),
                        fieldWithPath("projects[].assignedTasks[].deadline").type(JsonFieldType.STRING).description("배정된 task의 deadline")
                )
        );
    }

}
