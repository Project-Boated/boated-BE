package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static com.projectboated.backend.common.data.BasicDataUploadFile.ORIGINAL_FILE_NAME;
import static com.projectboated.backend.common.data.BasicDataUploadFile.SAVE_FILE_NAME;

public class BaseUploadFileTest extends BaseAccountTest{

    protected UploadFile createUploadFile() {
        return UploadFile.builder()
                .saveFileName(SAVE_FILE_NAME)
                .originalFileName(ORIGINAL_FILE_NAME)
                .build();
    }

    protected UploadFile createUploadFile(Long id) {
        UploadFile uploadFile = UploadFile.builder()
                .id(id)
                .saveFileName(SAVE_FILE_NAME)
                .originalFileName(ORIGINAL_FILE_NAME)
                .mediaType(MediaType.TEXT_PLAIN_VALUE)
                .build();
        uploadFile.changeCreatedDate(LocalDateTime.now());
        return uploadFile;
    }

}
