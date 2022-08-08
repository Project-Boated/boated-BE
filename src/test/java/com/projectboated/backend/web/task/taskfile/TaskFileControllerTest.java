package com.projectboated.backend.web.task.taskfile;

import com.projectboated.backend.common.basetest.AcceptanceTest;
import com.projectboated.backend.common.utils.AccountTestUtils;
import com.projectboated.backend.common.utils.ProjectTestUtils;
import com.projectboated.backend.common.utils.TaskTestUtils;
import com.projectboated.backend.infra.aws.AwsS3Service;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.stream.IntStream;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static com.projectboated.backend.common.data.BasicDataTask.*;
import static com.projectboated.backend.web.task.taskfile.document.TaskFileControllerDocument.documentUploadTaskFile;
import static io.restassured.RestAssured.given;


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

}