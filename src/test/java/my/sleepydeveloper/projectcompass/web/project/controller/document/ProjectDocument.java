package my.sleepydeveloper.projectcompass.web.project.controller.document;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class ProjectDocument {

    public static RestDocumentationFilter documentProjectCreate() {
        return document("project-create",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("프로젝트 이름"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("프로젝트 설명")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 프로젝트의 id")
                )
        );
    }

    public static RestDocumentationFilter documentProjectUpdate() {
        return document("project-update",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("업데이트할 프로젝트 이름(생략가능)"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("업데이트할 프로젝트 설명(생략가능)")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("업데이트된 프로젝트의 id")
                )
        );
    }

    public static RestDocumentationFilter documentProjectMy() {
        return document("project-my-retrieve",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("projects").type(JsonFieldType.ARRAY).description("내 project목록"),
                        fieldWithPath("projects[].name").type(JsonFieldType.STRING).description("프로젝트 이름"),
                        fieldWithPath("projects[].description").type(JsonFieldType.STRING).description("프로젝트 설명")
                )
        );
    }

    public static RestDocumentationResultHandler documentProjectRetrieveCrews() {
        return MockMvcRestDocumentation.document("project-crews-retrieve",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("crews").type(JsonFieldType.ARRAY).description("crew 목록"),
                        fieldWithPath("crews[].username").type(JsonFieldType.STRING).description("아이디"),
                        fieldWithPath("crews[].nickname").type(JsonFieldType.STRING).description("닉네임")
                )
        );
    }

    public static RestDocumentationResultHandler documentProjectUpdateCaptain() {
        return MockMvcRestDocumentation.document("project-captain-update",
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 id")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                requestFields(
                        fieldWithPath("newCaptainUsername").type(JsonFieldType.STRING).description("바꾸고 싶은 captain의 username")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("새로운 captain의 userId")
                )
        );
    }

    public static RestDocumentationFilter documentProjectInviteCrew() {
        return document("project-crew-invite",
                pathParameters(
                        parameterWithName("projectId").description("target 프로젝트의 id")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                requestFields(
                        fieldWithPath("username").type(JsonFieldType.STRING).description("초대하고 싶은 Account의 username")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 invitation의 id")
                )
        );
    }

}
