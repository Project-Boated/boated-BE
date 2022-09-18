package com.projectboated.backend.project.controller.document;

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

public abstract class ProjectMyDocument {

    public static RestDocumentationResultHandler documentProjectMyRetrieve() {
        return document("project-my-retrieve",
                preprocessResponse(prettyPrint()),
                requestParameters(
                        parameterWithName("captain").description("captain인 경우 조회(term: 종료된 프로젝트, not-term: 종료되지 않은 프로젝트)"),
                        parameterWithName("crew").description("crew인 경우 조회(term: 종료된 프로젝트, not-term: 종료되지 않은 프로젝트)"),
                        parameterWithName("page").description("조회할 page number"),
                        parameterWithName("size").description("조회할 page size"),
                        parameterWithName("sort").description("sort하는 기준, (name, deadline, createdDate) (asc, desc), ex)sort=name,desc")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("page").type(JsonFieldType.NUMBER).description("페이지 number"),
                        fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 size"),
                        fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 목록이 존재하는가?"),
                        fieldWithPath("content").type(JsonFieldType.ARRAY).description("project목록"),
                        fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("프로젝트 id"),
                        fieldWithPath("content[].name").type(JsonFieldType.STRING).description("프로젝트 이름"),
                        fieldWithPath("content[].description").type(JsonFieldType.STRING).description("프로젝트 설명"),
                        fieldWithPath("content[].deadline").type(JsonFieldType.STRING).description("프로젝트 마감기한"),
                        fieldWithPath("content[].captain").type(JsonFieldType.OBJECT).description("Captain정보"),
                        fieldWithPath("content[].captain.id").type(JsonFieldType.NUMBER).description("Captain 고유id"),
                        fieldWithPath("content[].captain.nickname").type(JsonFieldType.STRING).description("Captain 닉네임"),
                        fieldWithPath("content[].crews").type(JsonFieldType.ARRAY).description("Crew원들 정보"),
                        fieldWithPath("content[].crews[].id").type(JsonFieldType.NUMBER).description("Crew원 고유id"),
                        fieldWithPath("content[].crews[].nickname").type(JsonFieldType.STRING).description("Crew원 닉네임"),
                        fieldWithPath("content[].terminated").type(JsonFieldType.BOOLEAN).description("종료된 프로젝트이면 true, 아니면 false"),
                        fieldWithPath("content[].dday").type(JsonFieldType.NUMBER).description("dday"),
                        fieldWithPath("content[].totalDay").type(JsonFieldType.NUMBER).description("프로젝트 수행시간")
                )
        );
    }

}
