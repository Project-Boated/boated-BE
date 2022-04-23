package my.sleepydeveloper.projectcompass.domain.uploadfile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UploadFileService {

    private final UploadFileRepository uploadFileRepository;

    public void save(UploadFile uploadFile) {
        uploadFileRepository.save(uploadFile);
    }
}
