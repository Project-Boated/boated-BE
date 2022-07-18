package com.projectboated.backend.domain.account.profileimage.service;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.account.account.repository.AccountRepository;
import com.projectboated.backend.domain.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.domain.account.account.service.exception.AccountProfileImageFileNotExist;
import com.projectboated.backend.domain.account.profileimage.entity.ProfileImage;
import com.projectboated.backend.domain.account.profileimage.entity.UploadFileProfileImage;
import com.projectboated.backend.domain.account.profileimage.repository.ProfileImageRepository;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.infra.aws.AwsS3ProfileImageService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileImageService {

    private final AwsS3ProfileImageService awsS3ProfileImageService;
    private final ProfileImageRepository profileImageRepository;
    private final AccountRepository accountRepository;


    @Transactional
    public ProfileImage save(ProfileImage profileImage) {
        return profileImageRepository.save(profileImage);
    }

    @Transactional
    public void updateProfileImage(Long accountId, MultipartFile file) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        UploadFile uploadFile = UploadFile.builder()
                .originalFileName(file.getOriginalFilename())
                .saveFileName(UUID.randomUUID().toString())
                .mediaType(file.getContentType())
                .build();

        UploadFileProfileImage uploadFileProfileImage = new UploadFileProfileImage(uploadFile);
        profileImageRepository.save(uploadFileProfileImage);

        account.changeProfileImage(uploadFileProfileImage);
        awsS3ProfileImageService.uploadProfileImage(account, uploadFileProfileImage, file);
    }

    public String getProfileUrl(Long accountId, String hostUrl, boolean isProxy) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        ProfileImage profileImage = (ProfileImage) Hibernate.unproxy(account.getProfileImage());
        if (profileImage == null) {
            return null;
        }

        return profileImage.getUrl(hostUrl, isProxy);
    }

    @Transactional
    public void deleteProfileImageFile(Account account) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if(findAccount.getProfileImage() == null) {
            throw new AccountProfileImageFileNotExist(ErrorCode.ACCOUNT_PROFILE_IMAGE_FILE_NOT_EXIST);
        }

        ProfileImage profileImage = (ProfileImage) Hibernate.unproxy(findAccount.getProfileImage());
        if (profileImage instanceof UploadFileProfileImage uploadFileProfileImage) {
            awsS3ProfileImageService.deleteProfileImage(findAccount.getId(), uploadFileProfileImage);
        }

        findAccount.changeProfileImage(null);
    }
}
