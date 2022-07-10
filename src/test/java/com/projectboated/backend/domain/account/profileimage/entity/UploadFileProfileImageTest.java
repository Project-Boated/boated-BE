package com.projectboated.backend.domain.account.profileimage.entity;

import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.common.data.BasicDataUploadFile.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UploadFileProfileImage : Entity 단위 테스트")
class UploadFileProfileImageTest {
    
    @Test
    void 생성자_uploadfileProfileImage생성_return_생성된생성자_uploadfileProfileImage() {
        // Given
        UploadFile uploadFile = UploadFile.builder()
                .originalFileName(ORIGINAL_FILE_NAME)
                .saveFileName(SAVE_FILE_NAME)
                .mediaType(MEDIATYPE)
                .build();
        
        // When
        UploadFileProfileImage profileImage = new UploadFileProfileImage(uploadFile);

        // Then
        assertThat(profileImage.getUploadFile()).isEqualTo(uploadFile);
    }

}