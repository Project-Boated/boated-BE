package org.projectboated.backend.domain.profileimage.repository;

import org.projectboated.backend.domain.profileimage.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
