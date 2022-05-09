package my.sleepydeveloper.projectcompass.domain.profileimage.repository;

import my.sleepydeveloper.projectcompass.domain.profileimage.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
