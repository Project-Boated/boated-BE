package com.projectboated.backend.utils.base.repository;

import com.projectboated.backend.account.profileimage.repository.ProfileImageRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ProfileImageRepositoryTest extends AccountRepositoryTest {
    @Autowired
    protected ProfileImageRepository profileImageRepository;
}
