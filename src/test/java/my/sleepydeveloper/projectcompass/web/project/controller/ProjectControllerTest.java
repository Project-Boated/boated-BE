package my.sleepydeveloper.projectcompass.web.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import my.sleepydeveloper.projectcompass.common.basetest.AcceptanceTest;
import my.sleepydeveloper.projectcompass.common.utils.AccountProjectTestUtils;
import my.sleepydeveloper.projectcompass.common.utils.AccountTestUtils;
import my.sleepydeveloper.projectcompass.common.utils.ProjectTestUtils;
import my.sleepydeveloper.projectcompass.domain.project.service.ProjectService;
import my.sleepydeveloper.projectcompass.web.project.dto.InviteCrewRequest;
import my.sleepydeveloper.projectcompass.web.project.dto.ProjectSaveRequest;
import my.sleepydeveloper.projectcompass.web.project.dto.ProjectUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.RestAssured.given;
import static my.sleepydeveloper.projectcompass.common.data.BasicAccountData.*;
import static my.sleepydeveloper.projectcompass.common.data.BasicProjectData.*;
import static my.sleepydeveloper.projectcompass.web.project.controller.document.ProjectDocument.*;
import static org.hamcrest.Matchers.notNullValue;

@AutoConfigureMockMvc
class ProjectControllerTest extends AcceptanceTest {

    @Autowired
    AccountTestUtils accountTestUtils;

    @Autowired
    ProjectTestUtils projectTestUtils;

    @Autowired
    AccountProjectTestUtils accountProjectTestUtils;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProjectService projectService;

    final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void save_프로젝트생성_정상() throws Exception {
        // Given
        accountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        // When
        // Then
        given(this.spec)
                .filter(documentProjectCreate())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .cookie(cookie)
                .body(new ProjectSaveRequest(projectName, projectDescription))
        .when()
                .port(port)
                .post("/api/projects")
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("id", notNullValue());
    }

    @Test
    void save_같은account에같은projectname_오류발생() throws Exception {
        // Given
        accountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        ProjectTestUtils.createProject(port, cookie, projectName, projectDescription);

        // When
        // Then
        given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie(cookie)
            .body(new ProjectSaveRequest(projectName, projectDescription))
        .when()
            .port(port)
            .post("/api/projects")
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    void update_모든필드업데이트_정상() throws Exception {
        // Given
        accountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, projectName, projectDescription).jsonPath().get("id");

        // When
        // Then
        given(this.spec)
                .filter(documentProjectUpdate())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .cookie(cookie)
                .body(new ProjectUpdateRequest(updateProjectName, updateProjectDescription))
        .when()
                .port(port)
                .patch("/api/projects/" + projectId)
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void myProject_내프로젝트조회_정상() throws Exception {
        // Given
        accountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        for (int i = 0; i < 3; i++) {
            ProjectTestUtils.createProject(port, cookie, projectName, projectDescription);
        }

        // When
        // Then
        given(this.spec)
            .filter(documentProjectMy())
            .accept(ContentType.JSON)
            .cookie(cookie)
        .when()
            .port(port)
            .get("/api/projects/my")
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    // Invitation 구현이 끝나면 다시 테스팅, 현재는 테스트 불가
//    @Test
//    void getCrews_project에속한모든crew조회_성공() throws Exception {
//        // Given
//        Account account = accountTestUtils.getAccountFromSecurityContext();
//        Project project = projectTestUtils.createProject(account);
//        AccountProject accountProject = accountProjectTestUtils.createAccountRepository(account, project);
//
//        // When
//        // Then
//        mockMvc.perform(get("/api/projects/" + project.getId() + "/crews")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("crews").exists())
//                .andExpect(jsonPath("crews[0].username").value(account.getUsername()))
//                .andExpect(jsonPath("crews[0].nickname").value(account.getNickname()))
//                .andDo(documentProjectRetrieveCrews());
//    }

    // Invitation 구현이 끝나면 다시 테스팅, 현재는 테스트 불가
//    @Test
//    void updateCaptain_capatin업데이트_정상() throws Exception {
//        // Given
//        Account captain = accountTestUtils.getAccountFromSecurityContext();
//        Project project = projectTestUtils.createProject(captain, projectName, projectDescription);
//        String newCaptainUsername = "newCaptain";
//        String newCaptainPassword = "newCaptain";
//        String newCaptainNickname = "newCaptain";
//        Account newCaptain = accountTestUtils.signUp(newCaptainUsername, newCaptainPassword, newCaptainNickname, "ROLE_USER");
//        AccountProject accountProject = accountProjectTestUtils.createAccountRepository(newCaptain, project);
//
//        // When
//        // Then
//        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/projects/{projectId}/captain", project.getId())
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(new UpdateCaptainRequest(newCaptainUsername))))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("id").value(newCaptain.getId()))
//                .andDo(documentProjectUpdateCaptain());
//    }

    @Test
    void inviteCrew_account초대하기_정상() throws Exception {
        // Given
        accountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        String crewUsername = "crew";
        String crewNickname = "crew";
        accountTestUtils.createAccount(port, crewUsername, PASSWORD, crewNickname, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, projectName, projectDescription).jsonPath().get("id");

        // When
        // Then
        given(this.spec)
            .filter(documentProjectInviteCrew())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .cookie(cookie)
            .body(new InviteCrewRequest(crewUsername))
        .when()
            .port(port)
            .post("/api/projects/{projectId}/crews", projectId)
        .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat().body("id", notNullValue());
    }
}