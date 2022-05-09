package my.sleepydeveloper.projectcompass.domain.uploadfile.service;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.profileimage.entity.ProfileImage;
import my.sleepydeveloper.projectcompass.domain.uploadfile.entity.UploadFile;
import my.sleepydeveloper.projectcompass.domain.uploadfile.repository.UploadFileRepository;
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
