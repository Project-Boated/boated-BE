package com.projectboated.backend.account.profileimage.service;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.account.account.repository.AccountRepository;
import com.projectboated.backend.account.account.service.exception.AccountNotFoundException;
import com.projectboated.backend.account.account.service.exception.AccountProfileImageFileNotExist;
import com.projectboated.backend.account.profileimage.entity.ProfileImage;
import com.projectboated.backend.account.profileimage.entity.UploadFileProfileImage;
import com.projectboated.backend.account.profileimage.repository.ProfileImageRepository;
import com.projectboated.backend.infra.aws.AwsS3ProfileImageService;
import com.projectboated.backend.uploadfile.entity.UploadFile;
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
                .orElseThrow(AccountNotFoundException::new);

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

    public String getProfileUrl(Long accountId, String hostUrl) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        ProfileImage profileImage = (ProfileImage) Hibernate.unproxy(account.getProfileImage());
        if (profileImage == null) {
            return null;
        }
        return profileImage.getUrl(hostUrl);
    }

    @Transactional
    public void deleteProfileImageFile(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        if (account.getProfileImage() == null) {
            throw new AccountProfileImageFileNotExist();
        }

        ProfileImage profileImage = (ProfileImage) Hibernate.unproxy(account.getProfileImage());
        if (profileImage instanceof UploadFileProfileImage uploadFileProfileImage) {
            awsS3ProfileImageService.deleteProfileImage(account.getId(), uploadFileProfileImage);
        }

        account.changeProfileImage(null);
    }
}
