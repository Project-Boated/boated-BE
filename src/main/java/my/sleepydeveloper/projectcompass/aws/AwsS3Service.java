package my.sleepydeveloper.projectcompass.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;
import my.sleepydeveloper.projectcompass.aws.exception.FileUploadInterruptException;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.AccountNotFoundException;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.uploadfile.entity.UploadFile;
import my.sleepydeveloper.projectcompass.web.common.exception.CommonIOException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

@Service
@Transactional
public class AwsS3Service {

    private final AmazonS3 amazonS3;
    private final TransferManager transferManager;
    private String bucketName = "boated";
    private final AccountRepository accountRepository;


    public AwsS3Service(AmazonS3 amazonS3, AccountRepository accountRepository) {
        this.amazonS3 = amazonS3;
        this.transferManager = TransferManagerBuilder.standard()
                .withS3Client(amazonS3)
                .build();
        this.accountRepository = accountRepository;
    }

    public void uploadFile(String key, File file) {
        Upload upload = transferManager.upload(bucketName, key, file);
        try {
            upload.waitForUploadResult();
        } catch (InterruptedException e) {
            throw new FileUploadInterruptException(ErrorCode.FILE_UPLOAD_INTERRUPT, e);
        }
    }

    public void uploadMultipartFile(String key, MultipartFile multipartFile, ObjectMetadata objectMetadata) throws IOException {
        objectMetadata.setContentLength(multipartFile.getSize());
        Upload upload = transferManager.upload(bucketName, key, multipartFile.getInputStream(), objectMetadata);
        try {
            upload.waitForUploadResult();
        } catch (InterruptedException e) {
            throw new FileUploadInterruptException(ErrorCode.FILE_UPLOAD_INTERRUPT, e);
        }
    }

    public String uploadProfileImage(Account account, MultipartFile multipartFile, UploadFile uploadFile) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());

        String path = "profile/" + account.getId() + "/" + uploadFile.getSaveFileName() + "." + uploadFile.getExt();
        uploadMultipartFile(path, multipartFile, metadata);
        return path;
    }

    public byte[] getFile(String filename) throws IOException {
        S3Object object = amazonS3.getObject(bucketName, filename);
        return IOUtils.toByteArray(object.getObjectContent());
    }

    private String extractExt(String name) {
        return name.substring(name.lastIndexOf('.')+1);
    }

    public AwsS3File getProfileImageBytes(Account account) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        try {
            byte[] bytes = getFile("profile/" + findAccount.getId() + "/" + findAccount.getProfileImageFile().getSaveFileName() + "." + findAccount.getProfileImageFile().getExt());

            String fileName = URLEncoder.encode(findAccount.getProfileImageFile().getOriginalFileName() + "." + findAccount.getProfileImageFile().getExt(), "UTF-8").replaceAll("\\+", "%20");

            return new AwsS3File(bytes, findAccount.getProfileImageFile().getMediaType(), fileName);
        } catch (IOException e) {
            throw new CommonIOException(ErrorCode.COMMON_IO_EXCEPTION, e);
        }
    }
}
