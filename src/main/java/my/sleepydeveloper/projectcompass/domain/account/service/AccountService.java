package my.sleepydeveloper.projectcompass.domain.account.service;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.aws.AwsS3Service;
import my.sleepydeveloper.projectcompass.aws.exception.AccountProfileImageNotUploadFile;
import my.sleepydeveloper.projectcompass.domain.account.entity.KakaoAccount;
import my.sleepydeveloper.projectcompass.domain.account.entity.Role;
import my.sleepydeveloper.projectcompass.domain.common.exception.CommonIOException;
import my.sleepydeveloper.projectcompass.domain.exception.ErrorCode;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.account.exception.*;
import my.sleepydeveloper.projectcompass.domain.account.repository.AccountRepository;
import my.sleepydeveloper.projectcompass.domain.account.service.condition.AccountUpdateCond;
import my.sleepydeveloper.projectcompass.domain.profileimage.entity.ProfileImage;
import my.sleepydeveloper.projectcompass.domain.profileimage.entity.UploadFileProfileImage;
import my.sleepydeveloper.projectcompass.domain.profileimage.entity.UrlProfileImage;
import my.sleepydeveloper.projectcompass.domain.profileimage.repository.ProfileImageRepository;
import my.sleepydeveloper.projectcompass.domain.uploadfile.entity.UploadFile;
import my.sleepydeveloper.projectcompass.security.service.KakaoWebService;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final ProfileImageRepository profileImageRepository;
    private final PasswordEncoder passwordEncoder;
    private final KakaoWebService kakaoWebService;
    private final AwsS3Service awsS3Service;

    public Account findById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    @Transactional
    public Account save(Account account) {
        checkDuplicationExists(account);
        passwordEncode(account);
        return accountRepository.save(account);
    }

    private void checkDuplicationExists(Account account) {
        if (isExistsUsername(account)) {
            throw new AccountUsernameAlreadyExistsException(ErrorCode.ACCOUNT_USERNAME_EXISTS);
        }
        if (isExistsNickname(account.getNickname())) {
            throw new AccountNicknameAlreadyExistsException(ErrorCode.ACCOUNT_NICKNAME_EXISTS);
        }
    }

    private boolean isExistsUsername(Account account) {
        return accountRepository.existsByUsername(account.getUsername());
    }

    public boolean isExistsNickname(String nickname) {
        return accountRepository.existsByNickname(nickname);
    }

    private void passwordEncode(Account account) {
        account.updatePassword(passwordEncoder.encode(account.getPassword()));
    }

    @Transactional
    public void updateProfile(Account account, AccountUpdateCond accountUpdateCondition) {

        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!(account instanceof KakaoAccount)) {
            if (!checkVaildPassword(findAccount, accountUpdateCondition.getOriginalPassword())) {
                throw new AccountPasswordWrong(ErrorCode.ACCOUNT_PASSWORD_WRONG);
            }
        }

        if (!hasSameNickname(accountUpdateCondition, findAccount) &&
                checkDuplicatedNickname(accountUpdateCondition.getNickname())) {
            throw new AccountNicknameAlreadyExistsException(ErrorCode.ACCOUNT_NICKNAME_EXISTS);
        }

        if (accountUpdateCondition.getNewPassword() != null) {
            accountUpdateCondition.setNewPassword(passwordEncoder.encode(accountUpdateCondition.getNewPassword()));
        }

        if (accountUpdateCondition.getProfileImageFile() != null) {
            MultipartFile file = accountUpdateCondition.getProfileImageFile();
            UploadFile uploadFile = new UploadFile(file.getOriginalFilename(), UUID.randomUUID().toString(), file.getContentType(), "host");
            UploadFileProfileImage uploadFileProfileImage = new UploadFileProfileImage(uploadFile);
            try {
                awsS3Service.uploadProfileImage(account, accountUpdateCondition.getProfileImageFile(), uploadFileProfileImage);
            } catch (IOException e) {
                throw new CommonIOException(ErrorCode.COMMON_IO_EXCEPTION, e);
            }
            findAccount.updateProfileImage(uploadFileProfileImage);
        }

        findAccount.updateProfile(accountUpdateCondition.getNickname(),
                accountUpdateCondition.getNewPassword());
    }

    @Transactional
    public void deleteProfileImageFile(Account account) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if(findAccount.getProfileImage() == null)
            throw new AccountProfileImageFileNotExist(ErrorCode.ACCOUNT_PROFILE_IMAGE_FILE_NOT_EXIST);

        ProfileImage uploadFile = findAccount.getProfileImage();
        findAccount.updateProfileImage(null);
        profileImageRepository.delete(uploadFile);
    }

    private boolean hasSameNickname(AccountUpdateCond accountUpdateCondition, Account findAccount) {
        return Objects.equals(findAccount.getNickname(), accountUpdateCondition.getNickname());
    }

    private boolean checkVaildPassword(Account account, String password) {
        if (password == null) {
            return false;
        }
        return passwordEncoder.matches(password, account.getPassword());
    }


    @Transactional
    public void delete(Account account) {
        if (account instanceof KakaoAccount kakaoAccount) {
            kakaoWebService.disconnect(kakaoAccount.getKakaoId());
        }

        accountRepository.delete(account);
    }

    @Transactional
    public void updateNickname(Account account, String nickname) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!Objects.equals(findAccount.getNickname(), nickname)
                && checkDuplicatedNickname(nickname)) {
            throw new AccountNicknameAlreadyExistsException(ErrorCode.ACCOUNT_NICKNAME_EXISTS);
        }

        findAccount.updateNickname(nickname);
    }

    private boolean checkDuplicatedNickname(String nickname) {
        return nickname != null && accountRepository.existsByNickname(nickname);
    }

    @Transactional
    public void updateProfileImage(Account account, ProfileImage profileImage) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        findAccount.updateProfileImage(profileImage);
    }

    public String getProfileUrl(Account account, HttpServletRequest request) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
        ProfileImage profileImage = (ProfileImage) Hibernate.unproxy(findAccount.getProfileImage());

        String profileUrl = "";
        if(profileImage instanceof UploadFileProfileImage) {
            String host = request.getHeader("HOST");
            profileUrl = "http://" + host + "/api/account/profile/profile-image";
        } else if (profileImage instanceof UrlProfileImage urlProfileImage) {
            profileUrl = urlProfileImage.getUrl();
        }

        return profileUrl;
    }

    public void checkAccountProfileImageUploadFile(Account account) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
        ProfileImage profileImage = (ProfileImage) Hibernate.unproxy(findAccount.getProfileImage());

        if(!(profileImage instanceof UploadFileProfileImage)) {
            throw new AccountProfileImageNotUploadFile(ErrorCode.ACCOUNT_PROFILE_IMAGE_NOT_UPLOAD_FILE);
        }
    }

    public String getNickname(Account account) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
        return findAccount.getNickname();
    }

    public String getUsername(Account account) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
        return findAccount.getUsername();
    }

    public Set<Role> getRoles(Account account) {
        Account findAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
        return findAccount.getRoles();
    }
}
