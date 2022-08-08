package com.projectboated.backend.domain.task.taskfile.entity;

import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static com.projectboated.backend.common.data.BasicDataTask.TASK_ID;
import static com.projectboated.backend.common.data.BasicDataTaskFile.TASK_FILE_ID;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskUploadFile : Entity 단위 테스트")
class TaskUploadFileTest {

    @Test
    void 생성자_TaskUploadFile생성_return_생성된TaskUploadFile() {
        // Given
        Task task = Task.builder()
                .build();
        UploadFile uploadFile = UploadFile.builder()
                .originalFileName("original.exe")
                .saveFileName("save")
                .mediaType(MediaType.IMAGE_JPEG_VALUE)
                .build();

        // When
        TaskFile taskUploadFile = new TaskFile(TASK_FILE_ID, task, uploadFile);

        // Then
        assertThat(taskUploadFile.getId()).isEqualTo(TASK_FILE_ID);
        assertThat(taskUploadFile.getTask()).isEqualTo(task);
        assertThat(taskUploadFile.getUploadFile()).isEqualTo(uploadFile);
    }

    @Test
    void getKey_정상요청_return_key() {
        // Given
        Task task = Task.builder()
                .id(TASK_ID)
                .build();
        UploadFile uploadFile = UploadFile.builder()
                .originalFileName("original.exe")
                .saveFileName("save")
                .mediaType(MediaType.IMAGE_JPEG_VALUE)
                .build();

        TaskFile taskUploadFile = new TaskFile(TASK_FILE_ID, task, uploadFile);

        // When
        String result = taskUploadFile.getKey();

        // Then
        assertThat(result).isEqualTo("projects/tasks/" + task.getId() + "/save.exe");
    }

}