package org.projectboated.backend.domain.uploadfile.repository;

import org.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
}
