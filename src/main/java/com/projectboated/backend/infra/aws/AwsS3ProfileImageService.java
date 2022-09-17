package com.projectboated.backend.infra.aws;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.account.profileimage.entity.ProfileImage;
import com.projectboated.backend.account.profileimage.entity.UploadFileProfileImage;
import com.projectboated.backend.common.exception.ErrorCode;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.infra.aws.exception.AccountProfileImageNotUploadFile;
import com.projectboated.backend.infra.aws.exception.FileUploadInterruptException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
public class AwsS3ProfileImageService {

    private final AwsS3Service awsS3Service;
    private final AccountRepository accountRepository;

    public String uploadProfileImage(Account account, UploadFileProfileImage profileImage, MultipartFile multipartFile) {
        UploadFile uploadFile = profileImage.getUploadFile();
        String path = getProfileSavePath(account, uploadFile);
        awsS3Service.uploadFile(path, multipartFile);
        return path;
    }

    private String getProfileSavePath(Account account, UploadFile uploadFile) {
        return "profile/" + account.getId() + "/" + uploadFile.getSaveFileName() + "." + uploadFile.getExt();
    }

    private String getProfileSavePath(Long accountId, UploadFile uploadFile) {
        return "profile/" + accountId + "/" + uploadFile.getSaveFileName() + "." + uploadFile.getExt();
    }

    public void deleteProfileImage(Long accountId, UploadFileProfileImage profileImage) {
        String profileSavePath = getProfileSavePath(accountId, profileImage.getUploadFile());
        awsS3Service.deleteFile(profileSavePath);
    }

    public AwsS3File getProfileImageBytes(Account account) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        ProfileImage profileImage = (ProfileImage) Hibernate.unproxy(findAccount.getProfileImage());
        if (!(profileImage instanceof UploadFileProfileImage)) {
            throw new AccountProfileImageNotUploadFile(ErrorCode.ACCOUNT_PROFILE_IMAGE_NOT_UPLOAD_FILE);
        }

        try {
            UploadFile uploadFile = ((UploadFileProfileImage) profileImage).getUploadFile();
            byte[] bytes = awsS3Service.getBytes(getProfileSavePath(findAccount, uploadFile));

            String fileName = URLEncoder.encode(uploadFile.getOriginalFileName() + "." + uploadFile.getExt(), "UTF-8").replaceAll("\\+", "%20");

            return new AwsS3File(fileName, uploadFile.getMediaType(), bytes);
        } catch (IOException e) {
            throw new FileUploadInterruptException(ErrorCode.COMMON_IO_EXCEPTION, e);
        }
    }
}
