package my.sleepydeveloper.projectcompass.web.project.controller.document;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KanbanDocument {

    public static RestDocumentationFilter documentKanbanLaneCreate() {
        return document("kanban-lane-create",
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

}
