package com.projectboated.backend.common.basetest.base;

import com.projectboated.backend.domain.uploadfile.entity.UploadFile;

import static com.projectboated.backend.common.data.BasicDataUploadFile.ORIGINAL_FILE_NAME;
import static com.projectboated.backend.common.data.BasicDataUploadFile.SAVE_FILE_NAME;

public class BaseUploadFileTest extends BaseAccountTest{

    protected UploadFile createUploadFile(Long id) {
        return UploadFile.builder()
                .id(id)
                .saveFileName(SAVE_FILE_NAME)
                .originalFileName(ORIGINAL_FILE_NAME)
                .build();
    }

}
