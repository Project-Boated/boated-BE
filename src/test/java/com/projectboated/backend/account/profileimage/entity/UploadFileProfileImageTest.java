package com.projectboated.backend.account.profileimage.entity;

import com.projectboated.backend.account.profileimage.entity.exception.ProfileImageNeedsHostUrlException;
import com.projectboated.backend.uploadfile.entity.UploadFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.utils.data.BasicDataUploadFile.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void getUrl_hostUrl주어짐isProxy_return_proxyUrl() {
        // Given
        UploadFile uploadFile = UploadFile.builder()
                .originalFileName(ORIGINAL_FILE_NAME)
                .saveFileName(SAVE_FILE_NAME)
                .mediaType(MEDIATYPE)
                .build();
        UploadFileProfileImage profileImage = new UploadFileProfileImage(uploadFile);

        // When
        String result = profileImage.getUrl("host");

        // Then
        assertThat(result).startsWith("http://host/api/account/profile/profile-image?hash=");
    }

}