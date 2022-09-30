package com.projectboated.backend.utils.base.base;

import com.projectboated.backend.uploadfile.entity.UploadFile;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static com.projectboated.backend.utils.data.BasicDataUploadFile.*;

public class BaseUploadFileTest extends BaseAccountTest {

    protected UploadFile createUploadFile() {
        return UploadFile.builder()
                .saveFileName(SAVE_FILE_NAME)
                .originalFileName(ORIGINAL_FILE_NAME)
                .fileSize(FILE_SIZE)
                .build();
    }

    protected UploadFile createUploadFile(Long id) {
        UploadFile uploadFile = UploadFile.builder()
                .id(id)
                .saveFileName(SAVE_FILE_NAME)
                .originalFileName(ORIGINAL_FILE_NAME)
                .mediaType(MediaType.TEXT_PLAIN_VALUE)
                .fileSize(FILE_SIZE)
                .build();
        uploadFile.changeCreatedDate(LocalDateTime.now());
        return uploadFile;
    }

}
