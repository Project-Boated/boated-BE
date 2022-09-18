package com.projectboated.backend.ganttchart.controller.document;

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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

public abstract class GanttChartDocument {

    public static RestDocumentationResultHandler documentMyGanttChartRetrieve() {
        return document("gantt-chart-my-retrieve",
                preprocessResponse(prettyPrint()),
                requestParameters(
                        parameterWithName("year").description("조회할 연도"),
                        parameterWithName("month").description("조회할 월")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("projects").type(JsonFieldType.ARRAY).description("프로젝트 목록"),
                        fieldWithPath("projects[].id").type(JsonFieldType.NUMBER).description("프로젝트 고유번호"),
                        fieldWithPath("projects[].name").type(JsonFieldType.STRING).description("프로젝트 이름"),
                        fieldWithPath("projects[].createdDate").type(JsonFieldType.STRING).description("프로젝트 만든날짜"),
                        fieldWithPath("projects[].deadline").type(JsonFieldType.STRING).description("프로젝트 deadline"),
                        fieldWithPath("projects[].period").type(JsonFieldType.NUMBER).description("프로젝트 총 수행시간"),
                        fieldWithPath("projects[].assignedTasks").type(JsonFieldType.ARRAY).description("배정된 task목록"),
                        fieldWithPath("projects[].assignedTasks[].id").type(JsonFieldType.NUMBER).description("배정된 task의 고유번호"),
                        fieldWithPath("projects[].assignedTasks[].name").type(JsonFieldType.STRING).description("배정된 task의 이름"),
                        fieldWithPath("projects[].assignedTasks[].createdDate").type(JsonFieldType.STRING).description("배정된 task의 만든날짜"),
                        fieldWithPath("projects[].assignedTasks[].deadline").type(JsonFieldType.STRING).description("배정된 task의 deadline"),
                        fieldWithPath("projects[].assignedTasks[].period").type(JsonFieldType.NUMBER).description("배정된 task의 총 수행시간")
                )
        );
    }

}
