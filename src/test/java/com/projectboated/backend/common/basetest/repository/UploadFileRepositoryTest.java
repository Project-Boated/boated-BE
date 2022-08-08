package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.domain.uploadfile.repository.UploadFileRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static com.projectboated.backend.common.data.BasicDataUploadFile.*;

public class UploadFileRepositoryTest extends TaskFileRepositoryTest {

    @Autowired
    protected UploadFileRepository uploadFileRepository;

    public UploadFile insertDefaultUploadFile() {
        return uploadFileRepository.save(UploadFile.builder()
                .saveFileName(SAVE_FILE_NAME)
                .originalFileName(ORIGINAL_FILE_NAME)
                .mediaType(MEDIATYPE)
                .build());
    }

}