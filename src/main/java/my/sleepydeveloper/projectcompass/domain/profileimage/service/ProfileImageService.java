package my.sleepydeveloper.projectcompass.domain.profileimage.service;

import lombok.RequiredArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.profileimage.entity.ProfileImage;
import my.sleepydeveloper.projectcompass.domain.profileimage.repository.ProfileImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileImageService {

    private final ProfileImageRepository profileImageRepository;

    @Transactional
    public ProfileImage save(ProfileImage profileImage) {
        return profileImageRepository.save(profileImage);
    }

}
