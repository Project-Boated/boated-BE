package com.projectboated.backend.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;
import com.projectboated.backend.aws.exception.FileUploadInterruptException;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Transactional
public class AwsS3Service {

    @Value("${cloud.aws.s3.bucketName}")
    private String BUCKET_NAME;
    private final AmazonS3 amazonS3;
    private final TransferManager transferManager;

    public AwsS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
        this.transferManager = TransferManagerBuilder.standard()
                .withS3Client(amazonS3)
                .build();
    }

    public void uploadFile(String key, File file) {
        Upload upload = transferManager.upload(BUCKET_NAME, key, file);
        try {
            upload.waitForUploadResult();
        } catch (InterruptedException e) {
            throw new FileUploadInterruptException(ErrorCode.FILE_UPLOAD_INTERRUPT, e);
        }
    }

    public void deleteFile(String key) {
        amazonS3.deleteObject(BUCKET_NAME, key);
    }

    public void uploadMultipartFile(String key, MultipartFile multipartFile) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        Upload upload = transferManager.upload(BUCKET_NAME, key, multipartFile.getInputStream(), objectMetadata);
        try {
            upload.waitForUploadResult();
        } catch (InterruptedException e) {
            throw new FileUploadInterruptException(ErrorCode.FILE_UPLOAD_INTERRUPT, e);
        }
    }

    public byte[] getBytes(String filename) throws IOException {
        S3Object object = amazonS3.getObject(BUCKET_NAME, filename);
        return IOUtils.toByteArray(object.getObjectContent());
    }
}
