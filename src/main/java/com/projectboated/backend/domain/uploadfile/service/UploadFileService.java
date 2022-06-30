package com.projectboated.backend.domain.uploadfile.service;

import com.projectboated.backend.domain.uploadfile.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UploadFileService {

    private final UploadFileRepository uploadFileRepository;

    @Transactional
    public void save(UploadFile uploadFile) {
        uploadFileRepository.save(uploadFile);
    }
}
