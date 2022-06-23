package org.projectboated.backend.domain.profileimage.service;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.account.exception.AccountNotFoundException;
import org.projectboated.backend.domain.account.repository.AccountRepository;
import org.projectboated.backend.domain.account.service.AccountProfileImageService;
import org.projectboated.backend.domain.common.exception.CommonIOException;
import org.projectboated.backend.domain.exception.ErrorCode;
import org.projectboated.backend.domain.profileimage.entity.ProfileImage;
import org.projectboated.backend.domain.profileimage.entity.UploadFileProfileImage;
import org.projectboated.backend.domain.profileimage.repository.ProfileImageRepository;
import org.projectboated.backend.domain.uploadfile.entity.UploadFile;
import org.projectboated.backend.domain.uploadfile.service.UploadFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileImageService {

    private final ProfileImageRepository profileImageRepository;
    private final AccountRepository accountRepository;
    private final UploadFileService uploadFileService;
    private final AwsS3ProfileImageService awsS3ProfileImageService;
    private final AccountProfileImageService accountProfileImageService;

    @Transactional
    public ProfileImage save(ProfileImage profileImage) {
        return profileImageRepository.save(profileImage);
    }

    @Transactional
    public void updateProfileImage(Long accountId, MultipartFile file) {
        Account findAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        try {
            UploadFile uploadFile = new UploadFile(file.getOriginalFilename(), UUID.randomUUID().toString(), file.getContentType());
            UploadFileProfileImage uploadFileProfileImage = new UploadFileProfileImage(uploadFile);
            uploadFileService.save(uploadFile);
            save(uploadFileProfileImage);

            awsS3ProfileImageService.uploadProfileImage(findAccount, uploadFileProfileImage, file);
            accountProfileImageService.updateProfileImage(findAccount, uploadFileProfileImage);
        } catch (IOException e) {
            throw new CommonIOException(ErrorCode.COMMON_IO_EXCEPTION, e);
        }
    }
}
