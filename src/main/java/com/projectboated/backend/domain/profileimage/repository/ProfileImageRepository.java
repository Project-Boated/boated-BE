package com.projectboated.backend.domain.profileimage.repository;

import com.projectboated.backend.domain.profileimage.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
