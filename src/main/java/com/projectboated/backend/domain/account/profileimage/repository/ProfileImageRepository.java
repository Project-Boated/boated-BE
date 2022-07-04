package com.projectboated.backend.domain.account.profileimage.repository;

import com.projectboated.backend.domain.account.profileimage.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {
}
