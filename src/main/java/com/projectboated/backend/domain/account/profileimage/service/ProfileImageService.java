package com.projectboated.backend.domain.account.profileimage.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.profileimage.entity.ProfileImage;
import com.projectboated.backend.domain.account.profileimage.entity.UploadFileProfileImage;
import com.projectboated.backend.domain.account.profileimage.repository.ProfileImageRepository;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.domain.uploadfile.service.UploadFileService;
import com.projectboated.backend.infra.aws.AwsS3ProfileImageService;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

        UploadFile uploadFile = new UploadFile(file.getOriginalFilename(), UUID.randomUUID().toString(), file.getContentType());
        UploadFileProfileImage uploadFileProfileImage = new UploadFileProfileImage(uploadFile);
        uploadFileService.save(uploadFile);
        save(uploadFileProfileImage);

        awsS3ProfileImageService.uploadProfileImage(findAccount, uploadFileProfileImage, file);
        accountProfileImageService.updateProfileImage(findAccount, uploadFileProfileImage);
    }
}
