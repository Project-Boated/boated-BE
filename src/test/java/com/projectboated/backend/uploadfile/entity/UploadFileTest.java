package com.projectboated.backend.uploadfile.entity;

import com.projectboated.backend.uploadfile.entity.exception.UploadFileNotFoundExt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.utils.data.BasicDataUploadFile.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("UploadFile : Entity 단위 테스트")
class UploadFileTest {

    @Test
    void 생성자_uploadFile생성_return_생성된uploadFile() {
        // Given
        // When
        UploadFile uploadFile = new UploadFile(UPLOAD_FILE_ID, ORIGINAL_FILE_NAME, SAVE_FILE_NAME, MEDIATYPE, FILE_SIZE);

        // Then
        assertThat(uploadFile.getOriginalFileName()).isEqualTo(ORIGINAL_FILE_NAME_NO_EXT);
        assertThat(uploadFile.getSaveFileName()).isEqualTo(SAVE_FILE_NAME);
        assertThat(uploadFile.getExt()).isEqualTo(EXT);
        assertThat(uploadFile.getMediaType()).isEqualTo(MEDIATYPE);
    }

    @Test
    void 생성자_originalFileName에ext없는경우_예외발생() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> new UploadFile(UPLOAD_FILE_ID, ORIGINAL_FILE_NAME_NO_EXT, SAVE_FILE_NAME, MEDIATYPE, FILE_SIZE))
                .isInstanceOf(UploadFileNotFoundExt.class);
    }

    @Test
    void getFullOriginalFileName() {
        // Given
        UploadFile uploadFile = UploadFile.builder()
                .originalFileName("file.exe")
                .build();

        // When
        String result = uploadFile.getFullOriginalFileName();

        // Then
        assertThat(result).isEqualTo("file.exe");
    }

}