package com.projectboated.backend.uploadfile.repository;

import com.projectboated.backend.uploadfile.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
}
