package com.projectboated.backend.domain.account.service;

import com.projectboated.backend.domain.account.entity.Account;
import com.projectboated.backend.domain.profileimage.service.AwsS3ProfileImageService;
import lombok.RequiredArgsConstructor;
import com.projectboated.backend.aws.exception.AccountProfileImageNotUploadFile;
import com.projectboated.backend.domain.account.exception.AccountNotFoundException;
import com.projectboated.backend.domain.account.exception.AccountProfileImageFileNotExist;
import com.projectboated.backend.domain.account.exception.AccountProfileImageNotSupportTypeException;
import com.projectboated.backend.domain.account.repository.AccountRepository;
import com.projectboated.backend.domain.common.exception.ErrorCode;
import com.projectboated.backend.domain.profileimage.entity.ProfileImage;
import com.projectboated.backend.domain.profileimage.entity.UploadFileProfileImage;
import com.projectboated.backend.domain.profileimage.entity.UrlProfileImage;
import com.projectboated.backend.domain.profileimage.repository.ProfileImageRepository;
import com.projectboated.backend.domain.uploadfile.entity.UploadFile;
import com.projectboated.backend.domain.uploadfile.repository.UploadFileRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountProfileImageService {

    private final AccountRepository accountRepository;
    private final UploadFileRepository uploadFileRepository;
    private final ProfileImageRepository profileImageRepository;
    private final AwsS3ProfileImageService awsS3ProfileImageService;

    @Transactional
    public void updateProfileImage(Account account, ProfileImage newProfileImage) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (findAccount.getProfileImage() != null) {
            ProfileImage profileImage = (ProfileImage) Hibernate.unproxy(findAccount.getProfileImage());
            if (profileImage instanceof UploadFileProfileImage uploadFileProfileImage) {
                awsS3ProfileImageService.deleteProfileImage(findAccount.getId(), uploadFileProfileImage);
                UploadFile uploadFile = uploadFileProfileImage.getUploadFile();
                uploadFileRepository.delete(uploadFile);
            }
            profileImageRepository.delete(profileImage);
        }

        if(newProfileImage instanceof UploadFileProfileImage uploadFileProfileImage) {
            UploadFile uploadFile = uploadFileProfileImage.getUploadFile();
            uploadFileRepository.save(uploadFile);
        }
        profileImageRepository.save(newProfileImage);

        findAccount.updateProfileImage(newProfileImage);
    }

    public String getProfileUrl(Account account, HttpServletRequest request, boolean isProxy) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
        ProfileImage profileImage = (ProfileImage) Hibernate.unproxy(findAccount.getProfileImage());

        if (profileImage == null) {
            return null;
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();
        uriBuilder.scheme("http");
        uriBuilder.queryParam("dummy", UUID.randomUUID().toString());

        if(profileImage instanceof UploadFileProfileImage) {
            if(isProxy)
                uriBuilder.host("localhost:3000");
            else
                uriBuilder.host(request.getHeader("HOST"));
            uriBuilder.path("/api/account/profile/profile-image");
            return uriBuilder.build().toString();
        } else if (profileImage instanceof UrlProfileImage urlProfileImage) {
            return urlProfileImage.getUrl() + "?dummy=" + UUID.randomUUID().toString();
        }

        throw new AccountProfileImageNotSupportTypeException(ErrorCode.ACCOUNT_PROFILE_IMAGE_NOT_SUPPORT_TYPE);
    }

    @Transactional
    public void deleteProfileImageFile(Account account) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if(findAccount.getProfileImage() == null)
            throw new AccountProfileImageFileNotExist(ErrorCode.ACCOUNT_PROFILE_IMAGE_FILE_NOT_EXIST);

        ProfileImage profileImage = (ProfileImage) Hibernate.unproxy(findAccount.getProfileImage());

        findAccount.updateProfileImage(null);

        if (profileImage instanceof UploadFileProfileImage uploadFileProfileImage) {
            awsS3ProfileImageService.deleteProfileImage(findAccount.getId(), uploadFileProfileImage);
            UploadFile uploadFile = uploadFileProfileImage.getUploadFile();
            uploadFileRepository.delete(uploadFile);
        }
        profileImageRepository.delete(profileImage);
    }

    public void checkAccountProfileImageUploadFile(Account account) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
        ProfileImage profileImage = (ProfileImage) Hibernate.unproxy(findAccount.getProfileImage());

        if(!(profileImage instanceof UploadFileProfileImage)) {
            throw new AccountProfileImageNotUploadFile(ErrorCode.ACCOUNT_PROFILE_IMAGE_NOT_UPLOAD_FILE);
        }
    }


}
