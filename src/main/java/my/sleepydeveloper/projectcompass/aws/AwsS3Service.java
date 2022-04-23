package my.sleepydeveloper.projectcompass.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.aws.exception.FileUploadInterruptException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class AwsS3Service {

    private final AmazonS3 amazonS3;
    private final TransferManager transferManager;

    public AwsS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
        this.transferManager = TransferManagerBuilder.standard()
                .withS3Client(amazonS3)
                .build();
    }

    public void uploadFile(String path, File file) {
        Upload upload = transferManager.upload("boated", path, file);
        try {
            upload.waitForUploadResult();
        } catch (InterruptedException e) {
            throw new FileUploadInterruptException(ErrorCode.FILE_UPLOAD_INTERRUPT, e);
        }
    }

}
