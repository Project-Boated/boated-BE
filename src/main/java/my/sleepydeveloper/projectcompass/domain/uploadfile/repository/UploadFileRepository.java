package my.sleepydeveloper.projectcompass.domain.uploadfile.repository;

import my.sleepydeveloper.projectcompass.domain.uploadfile.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
}
