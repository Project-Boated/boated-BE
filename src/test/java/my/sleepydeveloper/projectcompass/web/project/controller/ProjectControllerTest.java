package my.sleepydeveloper.projectcompass.web.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import my.sleepydeveloper.projectcompass.common.basetest.AcceptanceTest;
import my.sleepydeveloper.projectcompass.common.utils.AccountProjectTestUtils;
import my.sleepydeveloper.projectcompass.common.utils.AccountTestUtils;
import my.sleepydeveloper.projectcompass.common.utils.InvitationTestUtils;
import my.sleepydeveloper.projectcompass.common.utils.ProjectTestUtils;
import my.sleepydeveloper.projectcompass.domain.project.service.ProjectService;
import my.sleepydeveloper.projectcompass.web.project.dto.CreateProjectRequest;
import my.sleepydeveloper.projectcompass.web.project.dto.PatchProjectRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
    ProjectTestUtils projectTestUtils;

    @Autowired
    AccountProjectTestUtils accountProjectTestUtils;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProjectService projectService;

    final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createProject_프로젝트생성_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        // When
        // Then
        given(this.spec)
                .filter(documentProjectCreate())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .cookie(cookie)
                .body(new CreateProjectRequest(PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE))
        .when()
                .port(port)
                .post("/api/projects")
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("id", notNullValue());
    }


    @Test
    void patchProject_모든필드업데이트_정상() throws Exception {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);

        String projectUpdateName = "updatedProjectName";
        String projectUpdateDescription = "updateDescription";

        // When
        // Then
        given(this.spec)
                .filter(documentProjectUpdate())
                .contentType(ContentType.JSON)
                .cookie(cookie)
                .body(new PatchProjectRequest(projectUpdateName, projectUpdateDescription))
        .when()
                .port(port)
                .patch("/api/projects/" + projectId)
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("id", notNullValue());
    }

    @Test
    void getMyCaptainProject_내가Captain인프로젝트조회_정상() throws Exception {
        // Given
        String crewUsername = "crewUsername";
        String crewNickname = "crewNickname";

        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, PASSWORD, crewNickname, PROFILE_IMAGE_URL);
        Cookie crewCookie = AccountTestUtils.login(port, crewUsername, PASSWORD);
        Cookie captainCookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        int projectId = ProjectTestUtils.createProject(port, captainCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        int invitationId = InvitationTestUtils.createInvitation(port, captainCookie, (long) projectId, crewNickname);
        InvitationTestUtils.acceptInvitation(port, crewCookie, (long)invitationId);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectMy())
            .accept(ContentType.JSON)
            .cookie(captainCookie)
        .when()
            .port(port)
            .get("/api/projects/my/captain")
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getMyCrewProject_내가Crew인프로젝트조회_정상() throws Exception {
        // Given
        String crewUsername = "crewUsername";
        String crewNickname = "crewNickname";

        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        AccountTestUtils.createAccount(port, crewUsername, PASSWORD, crewNickname, PROFILE_IMAGE_URL);
        Cookie crewCookie = AccountTestUtils.login(port, crewUsername, PASSWORD);
        Cookie captainCookie = AccountTestUtils.login(port, USERNAME, PASSWORD);

        int projectId = ProjectTestUtils.createProject(port, captainCookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        int invitationId = InvitationTestUtils.createInvitation(port, captainCookie, (long) projectId, crewNickname);
        InvitationTestUtils.acceptInvitation(port, crewCookie, (long)invitationId);

        // When
        // Then
        given(this.spec)
            .filter(documentProjectCrew())
            .accept(ContentType.JSON)
            .cookie(crewCookie)
        .when()
            .port(port)
            .get("/api/projects/my/crew")
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
}