package com.projectboated.backend.domain.uploadfile.entity;

import com.projectboated.backend.domain.uploadfile.entity.exception.UploadFileNotFoundExt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.common.data.BasicDataUploadFile.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("UploadFile : Entity 단위 테스트")
class UploadFileTest {

    @Test
    void 생성자_uploadFile생성_return_생성된uploadFile() {
        // Given
        // When
        UploadFile uploadFile = new UploadFile(ORIGINAL_FILE_NAME, SAVE_FILE_NAME, MEDIATYPE);

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
        assertThatThrownBy(() -> new UploadFile(ORIGINAL_FILE_NAME_NO_EXT, SAVE_FILE_NAME, MEDIATYPE))
                .isInstanceOf(UploadFileNotFoundExt.class);
    }

}