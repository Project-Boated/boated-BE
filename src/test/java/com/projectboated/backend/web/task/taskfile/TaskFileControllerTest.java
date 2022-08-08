package com.projectboated.backend.web.task.taskfile;

import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.utils.AccountTestUtils;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import com.projectboated.backend.common.utils.TaskFileTestUtils;
import com.projectboated.backend.common.utils.TaskTestUtils;
import com.projectboated.backend.infra.aws.AwsS3Service;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.stream.IntStream;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static com.projectboated.backend.web.task.taskfile.document.TaskFileControllerDocument.*;
import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.*;


class TaskFileControllerTest extends AcceptanceTest {

    @MockBean
    AwsS3Service awsS3Service;

    @Test
    void uploadTaskFile_파일업로드_정상() throws FileNotFoundException {
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        int[] taskIds = IntStream.range(0, 5)
                .map((i) -> TaskTestUtils.createTask(port, cookie, projectId, TASK_NAME + i, TASK_DESCRIPTION, TASK_DEADLINE))
                .toArray();

        given(this.spec)
            .filter(documentUploadTaskFile())
            .contentType(ContentType.MULTIPART)
            .multiPart("file", ResourceUtils.getFile("classpath:profile-image-test.png"), "image/jpg")
            .cookie(cookie)
        .when()
            .port(port)
            .post("/api/projects/{projectId}/tasks/{taskId}/files", projectId, taskIds[0])
        .then()
            .statusCode(HttpStatus.OK.value());
    }
    
    @Test
    void deleteTaskFile_정상요청_delete성공() throws FileNotFoundException {
        // Given
        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        int[] taskIds = IntStream.range(0, 5)
                .map((i) -> TaskTestUtils.createTask(port, cookie, projectId, TASK_NAME + i, TASK_DESCRIPTION, TASK_DEADLINE))
                .toArray();
        int taskFileId = TaskFileTestUtils.createTaskFile(port, cookie, projectId, taskIds[0], ResourceUtils.getFile("classpath:profile-image-test.png"), "image/jpg");

        // When
        // Then
        given(this.spec)
            .filter(documentTaskFileDelete())
            .cookie(cookie)
        .when()
            .port(port)
            .delete("/api/projects/{projectId}/tasks/{taskId}/files/{taskFileId}", projectId, taskIds[0], taskFileId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getTaskFile_정상요청_정상return() throws FileNotFoundException {
        // Given
        when(awsS3Service.getBytes(any())).thenReturn("good".getBytes());

        AccountTestUtils.createAccount(port, USERNAME, PASSWORD, NICKNAME, PROFILE_IMAGE_URL);
        Cookie cookie = AccountTestUtils.login(port, USERNAME, PASSWORD);
        int projectId = ProjectTestUtils.createProject(port, cookie, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
        int[] taskIds = IntStream.range(0, 5)
                .map((i) -> TaskTestUtils.createTask(port, cookie, projectId, TASK_NAME + i, TASK_DESCRIPTION, TASK_DEADLINE))
                .toArray();
        int taskFileId = TaskFileTestUtils.createTaskFile(port, cookie, projectId, taskIds[0], ResourceUtils.getFile("classpath:profile-image-test.png"), "image/jpg");

        // When
        // Then
        given(this.spec)
            .filter(documentTaskFileRetrieve())
            .cookie(cookie)
        .when()
            .port(port)
            .get("/api/projects/{projectId}/tasks/{taskId}/files/{taskFileId}", projectId, taskIds[0], taskFileId)
        .then()
            .statusCode(HttpStatus.OK.value());
    }

}