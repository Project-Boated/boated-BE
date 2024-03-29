package com.projectboated.backend.utils.base.repository;

import com.projectboated.backend.uploadfile.entity.UploadFile;
import com.projectboated.backend.uploadfile.repository.UploadFileRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UploadFileRepositoryTest extends TaskFileRepositoryTest {

    @Autowired
    protected UploadFileRepository uploadFileRepository;

    public UploadFile insertUploadFile() {
        return uploadFileRepository.save(createUploadFile());
    }

}
