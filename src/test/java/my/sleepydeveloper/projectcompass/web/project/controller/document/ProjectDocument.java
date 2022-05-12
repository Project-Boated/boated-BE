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
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
                        fieldWithPath("description").type(JsonFieldType.STRING).description("프로젝트 설명"),
                        fieldWithPath("deadline").type(JsonFieldType.STRING).description("프로젝트 기한 (yyyy-MM-dd HH:mm:ss)")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("만들어진 project의 id")
                )
        );
    }

    public static RestDocumentationFilter documentProjectUpdate() {
        return document("project-update",
                requestHeaders(
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
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("수정한 project의 id")
                )
        );
    }

    public static RestDocumentationFilter documentProjectCaptain() {
        return document("project-my-captain-retrieve",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("projects").type(JsonFieldType.ARRAY).description("project목록"),
                        fieldWithPath("projects[].id").type(JsonFieldType.NUMBER).description("프로젝트 id"),
                        fieldWithPath("projects[].name").type(JsonFieldType.STRING).description("프로젝트 이름"),
                        fieldWithPath("projects[].description").type(JsonFieldType.STRING).description("프로젝트 설명"),
                        fieldWithPath("projects[].deadline").type(JsonFieldType.STRING).description("프로젝트 마감기한"),
                        fieldWithPath("projects[].captain").type(JsonFieldType.OBJECT).description("Captain정보"),
                        fieldWithPath("projects[].captain.id").type(JsonFieldType.NUMBER).description("Captain 고유id"),
                        fieldWithPath("projects[].captain.nickname").type(JsonFieldType.STRING).description("Captain 닉네임"),
                        fieldWithPath("projects[].crews").type(JsonFieldType.ARRAY).description("Crew원들 정보"),
                        fieldWithPath("projects[].crews[].id").type(JsonFieldType.NUMBER).description("Crew원 고유id"),
                        fieldWithPath("projects[].crews[].nickname").type(JsonFieldType.STRING).description("Crew원 닉네임"),
                        fieldWithPath("projects[].terminated").type(JsonFieldType.BOOLEAN).description("종료된 프로젝트이면 true, 아니면 false")
                )
        );
    }

    public static RestDocumentationFilter documentProjectCrew() {
        return document("project-my-crew-retrieve",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("projects").type(JsonFieldType.ARRAY).description("project목록"),
                        fieldWithPath("projects[].id").type(JsonFieldType.NUMBER).description("프로젝트 id"),
                        fieldWithPath("projects[].name").type(JsonFieldType.STRING).description("프로젝트 이름"),
                        fieldWithPath("projects[].description").type(JsonFieldType.STRING).description("프로젝트 설명"),
                        fieldWithPath("projects[].deadline").type(JsonFieldType.STRING).description("프로젝트 마감기한"),
                        fieldWithPath("projects[].captain").type(JsonFieldType.OBJECT).description("Captain정보"),
                        fieldWithPath("projects[].captain.id").type(JsonFieldType.NUMBER).description("Captain 고유id"),
                        fieldWithPath("projects[].captain.nickname").type(JsonFieldType.STRING).description("Captain 닉네임"),
                        fieldWithPath("projects[].crews").type(JsonFieldType.ARRAY).description("Crew원들 정보"),
                        fieldWithPath("projects[].crews[].id").type(JsonFieldType.NUMBER).description("Crew원 고유id"),
                        fieldWithPath("projects[].crews[].nickname").type(JsonFieldType.STRING).description("Crew원 닉네임"),
                        fieldWithPath("projects[].terminated").type(JsonFieldType.BOOLEAN).description("종료된 프로젝트이면 true, 아니면 false")
                )
        );
    }

    public static RestDocumentationFilter documentProjectCaptainTerminated() {
        return document("project-my-captain-terminated-retrieve",
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("받을 MediaType")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("보낸 Content Type")
                ),
                responseFields(
                        fieldWithPath("projects").type(JsonFieldType.ARRAY).description("project목록"),
                        fieldWithPath("projects[].id").type(JsonFieldType.NUMBER).description("프로젝트 id"),
                        fieldWithPath("projects[].name").type(JsonFieldType.STRING).description("프로젝트 이름"),
                        fieldWithPath("projects[].description").type(JsonFieldType.STRING).description("프로젝트 설명"),
                        fieldWithPath("projects[].deadline").type(JsonFieldType.STRING).description("프로젝트 마감기한"),
                        fieldWithPath("projects[].captain").type(JsonFieldType.OBJECT).description("Captain정보"),
                        fieldWithPath("projects[].captain.id").type(JsonFieldType.NUMBER).description("Captain 고유id"),
                        fieldWithPath("projects[].captain.nickname").type(JsonFieldType.STRING).description("Captain 닉네임"),
                        fieldWithPath("projects[].crews").type(JsonFieldType.ARRAY).description("Crew원들 정보"),
                        fieldWithPath("projects[].crews[].id").type(JsonFieldType.NUMBER).description("Crew원 고유id"),
                        fieldWithPath("projects[].crews[].nickname").type(JsonFieldType.STRING).description("Crew원 닉네임"),
                        fieldWithPath("projects[].terminated").type(JsonFieldType.BOOLEAN).description("종료된 프로젝트이면 true, 아니면 false")
                )
        );
    }

    public static RestDocumentationFilter documentProjectTerminate() {
        return document("project-terminate",
                pathParameters(
                        parameterWithName("projectId").description("프로젝트 id")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("프로젝트 고유 id")
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

}
