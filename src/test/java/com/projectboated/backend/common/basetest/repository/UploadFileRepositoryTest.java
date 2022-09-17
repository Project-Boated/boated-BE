package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.domain.uploadfile.repository.UploadFileRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UploadFileRepositoryTest extends TaskFileRepositoryTest {

    @Autowired
    protected UploadFileRepository uploadFileRepository;

    public UploadFile insertUploadFile() {
        return uploadFileRepository.save(createUploadFile());
    }

}
